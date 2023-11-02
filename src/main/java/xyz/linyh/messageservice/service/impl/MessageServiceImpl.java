package xyz.linyh.messageservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.model.ReturnMsgVO;
import xyz.linyh.messageservice.service.MessageService;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    public void addOne(Message message) {
        mongoTemplate.save(message);
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
        ArrayList<Message> messages = new ArrayList<>();
        for (Long id : ids) {
            Message msg = createMsg(id, message);
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

    private Message createMsg(Long id,String message){
        Message messageEntity = new Message();
        messageEntity.setToUserId(id);
        messageEntity.setMsgContent(message);
        messageEntity.setCreateTime(new Date());
        messageEntity.setUpdateTime(new Date());
        messageEntity.setIsRead((short) 0);
        messageEntity.setFromUserId(0L);
        return messageEntity;
    }
}
