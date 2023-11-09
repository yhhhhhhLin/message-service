package xyz.linyh.messageservice.service;

import xyz.linyh.messageservice.entitys.Contact;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.model.ReturnMsgVO;
import xyz.linyh.messageservice.model.vo.ContactsVO;

import java.util.List;

public interface MessageService {
//    public void addOne(Long fromId,Long toId,String message);

    /**
     * 删
     * @param msgId
     */
    public void deleteOneById(String msgId);
    /**
     * 改
     * @param message
     */
    public void updateOne(Message message);

    /**
     * 查
     * @param studentId
     * @return
     */
    public Message findOneById(String studentId);

    public List<Message> findAll();

    /**
     * 将系统发送的消息保存到数据库
     * @param ids
     * @param message
     */
//    public void saveBroadcast(List<Long> ids, String message);

    /**
     * 获取
     * @param userId
     * @param sendUserId 如果为0，那么就是系统通知
     * @return
     */
//    public ReturnMsgVO getMsg(Long userId, Long sendUserId);

    /**
     * 获取某个用户的所有未读消息
     * @param id
     * @return
     */
//    Integer getNoReadCount(Long id);

    /**
     * 用户读取和某个用户的消息(更新未读数量)
     * @param readUserId
     * @param sendUserId
     * @param readCount
     * @return
     */
//    Boolean readMessage(Long readUserId, Long sendUserId, Integer readCount);

    /**
     * 获取某个用户的所有信息(顺序!)
     * @param id
     * @return
     */
//    List<List<Message>> getAllMsg(Long id);
    /**
     * 根据时间获取用户的聊天会话列表
     * @param userId 用户id
     * @param pageSize 页面大小
     * @param currentPage 当前页码
     * @return 查询结果
     */
    List<ContactsVO> getSessionByPage(Long userId, Integer pageSize, Integer currentPage);

    /**
     * 发送消息，判断是否有对应会话，如果没有，那么就创建对应会话然后保存session
     * @param userId
     * @param toUserId
     * @param test
     */
    void sendMsg(Long userId, Long toUserId, String test);

    /**
     * 获取某个用户的未读消息数量
     * @param id
     * @return
     */
    Long getUnreadCount(Long id);
}
