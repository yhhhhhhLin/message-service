package xyz.linyh.messageservice.service.impl;
import java.util.Date;

import com.mongodb.BasicDBObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import xyz.linyh.messageservice.entitys.Contact;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.entitys.contactConfig;
import xyz.linyh.messageservice.model.GroupMessagesResultVO;
import xyz.linyh.messageservice.model.ReturnMsgVO;
import xyz.linyh.messageservice.model.vo.ContactsVO;
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
     * 删
     *
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
     *
     * @param message
     */
    @Override
    public void updateOne(Message message) {
        mongoTemplate.save(message);

    }

    /**
     * 查
     *
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
     * 根据时间获取用户的聊天会话列表
     *
     * @param userId      用户id
     * @param pageSize    页面大小
     * @param currentPage 当前页码
     * @return 查询结果
     */
    @Override
    public List<ContactsVO> getSessionByPage(Long userId, Integer pageSize, Integer currentPage) {
//        1. 参数校验
        if (pageSize == null) {
            pageSize = 20;
        }
        if (currentPage == null) {
            currentPage = 1;
        }
        Query query2 = new Query(
                Criteria.where("userId").is(userId).and("status").is(0)).with(Sort.by(Sort.Order.desc("createTime")))
                .limit(pageSize).skip((long) (currentPage - 1) * pageSize);
        List<contactConfig> contactConfigs = mongoTemplate.find(query2, contactConfig.class);

        List<ContactsVO> contactsVOS = new ArrayList<>();
        for (contactConfig contactConfig : contactConfigs) {
            Query query = new Query(Criteria.where("id").is(contactConfig.getContactId()));

            List<Contact> contacts = mongoTemplate.find(query, Contact.class);
            if (contacts != null && contacts.size() > 0) {
                Contact contact = contacts.get(0);
                Long userId1 = contact.getUserId1();
                Long userId2 = contact.getUserId2();
                Long fromUserId = null;
                if(userId.equals(userId1)){
                    fromUserId = userId2;
                }else {
                    fromUserId = userId1;
                }
//                todo 查询用户服务,然后copy到对应vo里面
                ContactsVO contactsVO = new ContactsVO();
                BeanUtils.copyProperties(contacts.get(0), contactsVO);
//                userId1里面保存对面用户id
                contactsVO.setUserId1(fromUserId);
                contactsVO.setSetTop(contactConfig.getSetTop());
//                查找未读消息数量
//                按照顺序查找出，所有未读消息数量
                Query query1 = new Query(
                        Criteria.where("fromUserId").is(fromUserId)
                                .and("status").is(0).and("isRead").is(0)
                                .and("contactId").is(contacts.get(0).getId())).with(Sort.by(Sort.Order.desc("createTime")
                        )
                );

                long count = mongoTemplate.count(query1, Message.class);
                contactsVO.setUnReadCount(count);
                contactsVOS.add(contactsVO);
            }

        }

        return contactsVOS;

    }

    /**
     * 发送消息，判断是否有对应会话，如果没有，那么就创建对应会话然后保存session
     *
     * @param userId
     * @param toUserId
     * @param test
     */
    @Override
    public void sendMsg(Long userId, Long toUserId, String test) {
//        todo 还需要通过ws判断用户是否在线，如果在线就直接发送消息给用户
//        判断是否对应会话
        Query query = new Query(
                new Criteria().orOperator(Criteria.where("userId1").is(userId).and("userId2").is(toUserId), Criteria.where("userId1").is(toUserId).and("userId2").is(userId)));
        List<Contact> contacts = mongoTemplate.find(query, Contact.class);
//        如果没有那么创对应会话
        if (contacts == null || contacts.size() == 0) {
            Contact contact = new Contact();
            contact.setUserId1(userId);
            contact.setUserId2(2L);
            contact.setActiveTime(System.currentTimeMillis());
            contact.setLastMsgId("");
            contact.setSetTop(0);
            contact.setCreateTime(System.currentTimeMillis());
            contact.setUpdateTime(System.currentTimeMillis());
            Contact save = mongoTemplate.save(contact);
            contacts.add(save);

//            还需要创建对应contactConfig
            contactConfig contactConfig = new contactConfig();
            contactConfig.setUserId(userId);
            contactConfig.setContactId(contact.getId());
            contactConfig.setStatus(0);
            contactConfig.setSetTop(0);
            contactConfig.setCreateTime(System.currentTimeMillis());
            contactConfig.setUpdateTime(System.currentTimeMillis());
            mongoTemplate.save(contactConfig);


        }
//        然后根据会话id发送消息
        Message message = new Message();
        message.setContactId(contacts.get(0).getId());
        message.setFromUserId(userId);
        message.setContent(test);
        message.setReplyMsgId("");
        message.setStatus(0);
        message.setGapCount(-1);
        message.setIsRead(0);
        message.setType(1);
        message.setExtra("");
        message.setCreateTime(System.currentTimeMillis());
        message.setUpdateTime(System.currentTimeMillis());
        mongoTemplate.save(message);
//        更新前面contact的lastMsgId todo
        Query query2 = new Query(Criteria.where("id").is(contacts.get(0).getId()));
        Update update1 = new Update().set("lastMsgId", message.getId());
        mongoTemplate.updateFirst(query2, update1, Contact.class);
//        如果有新发送消息，那么需要修改config里面对应的删除字段
        Query query1 = new Query(Criteria.where("contactId").is(contacts.get(0).getId()));
        Update update = new Update().set("status", 0);
        mongoTemplate.updateFirst(query1, update, contactConfig.class);

    }

    /**
     * 获取某个用户的未读消息数量
     *
     * @param id
     * @return
     */
    @Override
    public Long getUnreadCount(Long id) {
        new Query(Criteria.where(""))
        mongoTemplate.count()
        return null;
    }


    /**
     * 将获取到的所有信息进行分组
     *
     * @param mappedResults
     * @return
     */
    private List<List<Message>> groupByToList(List<GroupMessagesResultVO> mappedResults) {

        List<List<Message>> groupList = new ArrayList<>();
        List<Message> listOne = new ArrayList<>();
        Long preFromUserId = null;

        for (int i = 0; i < mappedResults.size(); i++) {
//            如果这个的发送人和上一个一样的话，那么就存到ListOne中
            Message message = mappedResults.get(i).getGroupMessages();
            if (preFromUserId != null && preFromUserId.equals((message.getFromUserId()))) {
                listOne.add(message);
            } else {
//            如果前面的preToUserId为空那么就更新proToUser
//            如果preToUserId和不一样的话，那就把前面的存到groupList中，清空listOne，然后存一个到listOne中，再重新更新preToUserId
                if (preFromUserId != null) {
                    groupList.add(listOne);
                    listOne = new ArrayList<>();
                }
                listOne.add(message);
                preFromUserId = message.getFromUserId();
            }

        }
        groupList.add(listOne);
        return groupList;
    }

}
