package xyz.linyh.messageservice.service.impl;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.entitys.MessageSession;
import xyz.linyh.messageservice.model.GroupMessagesResultVO;
import xyz.linyh.messageservice.model.MessageSessionAndContent;
import xyz.linyh.messageservice.model.ReturnMsgVO;
import xyz.linyh.messageservice.service.MessageService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;


/**
 * 利用mongoTemplate来操作mongodb
 * @author lin
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 增
     * @param message
     */
    @Override
    public void addOne(Long sessionId,String message) {
        Message msg = createMsg(sessionId, message);
        mongoTemplate.save(msg);
    }

    /**
     * 删
     * @param msgId
     */
    @Override
    public void deleteOneById(String msgId) {
        Message msg = mongoTemplate.findById(msgId, Message.class);
        if (msg != null) {
            mongoTemplate.remove(msg);
        }
    }

    /**
     * 改
     * @param message
     */
    @Override
    public void updateOne(Message message) {
        mongoTemplate.save(message);

    }

    /**
     * 查
     * @param studentId
     * @return
     */
    @Override
    public Message findOneById(String studentId) {
        return mongoTemplate.findById(studentId, Message.class);
    }

    @Override
    public List<Message> findAll() {
        return mongoTemplate.findAll(Message.class);
    }

    /**
     * 发送系统统一消息
     *
     * @param ids
     */
    @Override
    public void saveBroadcast(List<Long> ids, String message) {
//        根据用户id获取对应的session
        Query query = new Query(Criteria.where("fromUserId").is(0).and("toUserId").in(ids));
        List<MessageSession> messageSessions = mongoTemplate.find(query, MessageSession.class);

        ArrayList<Message> messages = new ArrayList<>();
        for (MessageSession messageSession: messageSessions) {
            Message msg = createMsg( messageSession.getId(),message);
            messages.add(msg);
        }
//        批量插入,BulkMode.UNORDERED:表示并行处理，遇到错误时能继续执行不影响其他操作；
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,Message.class).insert(messages).execute();
    }

    /**
     * 获取用户和用户之间消息和未读消息数量
     * 还可以获取用户和系统消息之间的消息和未读数量
     *
     * @param userId
     * @param sendUserId 如果为0，那么就是系统通知
     * @return
     */
    @Override
    public ReturnMsgVO getMsg(Long userId, Long sendUserId) {
//        获取这个用户对应的所有消息
        Query query = new Query(
//                等于条件
                Criteria.where("userId").is(userId)
                        .and("sendUserId").is(sendUserId))
//                正序排序
                .with(Sort.by(Sort.Order.asc("createTime")));
        List<Message> messageList = mongoTemplate.find(query, Message.class);
        if(messageList==null || messageList.size()==0){
            return null;
        }

//        循环获取未读条数和封装到对应类，todo 因为查找后是按照时间顺序，可以优化为找到有读取的了，就可以不用继续找了
        Integer noReadCount = 0;
        for (Message message : messageList) {
            if(message.getIsRead()==0) {noReadCount++;}
        }

        ReturnMsgVO returnMsgVO = new ReturnMsgVO();
        returnMsgVO.setMessages(messageList);
        returnMsgVO.setUnreadMessage(noReadCount);
        returnMsgVO.setSendUserId(sendUserId);

        if(messageList.size()>=noReadCount){
            messageList = messageList.subList(messageList.size() - noReadCount, messageList.size());
        }

        List<String> ids = new ArrayList<>();
        for (Message message : messageList) {
            ids.add(message.getId());
        }

//        扫描然后批量更新isRead
        Query query1 = new Query(Criteria.where("id").in(ids));
        Update update = new Update().set("isRead",1);
        mongoTemplate.updateMulti(query1,update,Message.class);
        return returnMsgVO;
    }

    /**
     * 获取某个用户的所有未读消息
     *
     * @param id
     * @return
     */
    @Override
    public Integer getNoReadCount(Long id) {
        Query query = new Query(Criteria.where("user_id").is(id));
        List<Message> messages = mongoTemplate.find(query, Message.class);
//        获取所有未读的数量
        Integer count = 0;
        for (Message message : messages) {
            if(message.getIsRead()==0){
                count++;
            }
        }
        return count;
    }

    /**
     * 用户读取和某个用户的消息(更新未读数量)
     * @param readUserId
     * @param sendUserId
     * @param readCount
     * @return
     */
    @Override
    public Boolean readMessage(Long readUserId, Long sendUserId, Integer readCount) {
        Query query = new Query(Criteria.where("fromUserId").is(sendUserId).and("toUserId").is(readUserId));
//        todo 先全部改为已读
        Update update = new Update().set("isRead",1);
        mongoTemplate.updateMulti(query,update,Message.class);
        return true;
    }

    /**
     *按照时间查询这个用户和其他用户之间的聊天未读消息和数量
     * @param id
     * @return
     */
    @Override
    public List<List<Message>> getAllMsg(Long id) {
//     查询所有未读消息，然后按照发送人进行分组，然后对分组后消息里面的消息按照时间排序，晚发的排在后面，

//        1. 将所有未读消息找出来，然后按照时间进行排序
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("toUserId").is(id).and("isRead").is(0)),
                Aggregation.group("fromUserId").push("$$ROOT").as("groupMessages"),
                Aggregation.unwind("groupMessages"),
                Aggregation.sort(Sort.Direction.ASC, "groupMessages.createTime")
        );

        AggregationResults<GroupMessagesResultVO> aggregate = mongoTemplate.aggregate(aggregation, Message.class, GroupMessagesResultVO.class);

        List<GroupMessagesResultVO> mappedResults = aggregate.getMappedResults();
        if(mappedResults==null && mappedResults.size()==0){
            return null;
        }
        List<List<Message>> groupList =  groupByToList(mappedResults);
//        System.out.println(groupList);
//        再将数据按照最新发送消息进行排序
        groupList = sortByNewestMsg(groupList);

        return groupList;
    }

    /**
     * 找出用户最新聊天对话和对应的用户未读消息数量和最新未读消息
     *
     * @param id
     * @param page
     * @param pageSize
     */
    @Override
    public List<MessageSessionAndContent> getHistorySession(Long id, Integer page, Integer pageSize) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC));

        List<MessageSession> messageSessions = mongoTemplate.find(query, MessageSession.class);

//        一个一个会话去找对应的信息 todo 优化查询
        List<MessageSessionAndContent> messageSessionAndContents = new ArrayList<>();
        for(MessageSession messageSession :messageSessions){
            Long sessionId = messageSession.getId();
            Query query2 = new Query(Criteria.where("sessionId").is(sessionId)).with(Sort.by(Sort.Direction.ASC));
            List<Message> messages = mongoTemplate.find(query2, Message.class);

            MessageSessionAndContent messageSessionAndContent = new MessageSessionAndContent();
            messageSessionAndContent.setMessageContent(messages.get(messages.size()-1).getMsgContent());
            messageSessionAndContent.setUnReadCount(getSessionUnReadCount(messages));
            messageSessionAndContent.setId(messageSession.getId());
            messageSessionAndContent.setToUserId(messageSession.getToUserId());
            messageSessionAndContent.setFromUserId(messageSession.getFromUserId());
            messageSessionAndContent.setSetTop(messageSession.getSetTop());
            messageSessionAndContent.setCreateTime(messageSession.getCreateTime());
            messageSessionAndContent.setUpdateTime(messageSession.getUpdateTime());
            messageSessionAndContents.add(messageSessionAndContent);
        }

        return messageSessionAndContents;
    }

    /**
     * 获取某个会话里面的未读消息数量
     * @param messages
     * @return
     */
    private Integer getSessionUnReadCount(List<Message> messages) {

        Integer count = 0;
        for(Message m:messages){
            if (m.getIsRead()==0) {
                count++;
            }
        }

        return count;
    }

    /**
     *将所有数据按照最新消息进行排序
     * @param groupList
     * @return
     */
    private List<List<Message>> sortByNewestMsg(List<List<Message>> groupList) {
        // 使用Comparator.comparingLong()按照最新消息的时间戳进行排序
        Collections.sort(groupList, Comparator.comparingLong(list -> list.get(list.size() - 1).getCreateTime().getTime()));
        Collections.reverse(groupList);
        return groupList;
    }

    /**
     * 将获取到的所有信息进行分组
     * @param mappedResults
     * @return
     */
    private List<List<Message>> groupByToList(List<GroupMessagesResultVO> mappedResults) {

//        List<List<Message>> groupList = new ArrayList<>();
//        List<Message> listOne = new ArrayList<>();
//        Long preFromUserId = null;
//
//        for(int i = 0;i<mappedResults.size();i++){
////            如果这个的发送人和上一个一样的话，那么就存到ListOne中
//            Message message = mappedResults.get(i).getGroupMessages();
//            if(preFromUserId!=null && preFromUserId.equals((message.getFromUserId()))){
//                listOne.add(message);
//            }else{
////            如果前面的preToUserId为空那么就更新proToUser
////            如果preToUserId和不一样的话，那就把前面的存到groupList中，清空listOne，然后存一个到listOne中，再重新更新preToUserId
//                if(preFromUserId!=null){
//                    groupList.add(listOne);
//                    listOne = new ArrayList<>();
//                }
//                listOne.add(message);
//                preFromUserId=message.getFromUserId();
//            }
//
//        }
//        groupList.add(listOne);
        return null;
    }

    private Message createMsg(Long sessionId,String message){
        Message messageEntity = new Message();
        messageEntity.setSessionId(sessionId);
        messageEntity.setMsgContent(message);
        messageEntity.setIsRead((short)0);
        messageEntity.setCreateTime(new Date());
        messageEntity.setUpdateTime(new Date());

        return messageEntity;
    }
}
