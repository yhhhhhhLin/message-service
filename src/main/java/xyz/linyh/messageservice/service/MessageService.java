package xyz.linyh.messageservice.service;

import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.model.ReturnMsgVO;

import java.util.List;

public interface MessageService {
    public void addOne(Long fromId,Long toId,String message);

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
    public void saveBroadcast(List<Long> ids, String message);

    /**
     * 获取
     * @param userId
     * @param sendUserId 如果为0，那么就是系统通知
     * @return
     */
    public ReturnMsgVO getMsg(Long userId, Long sendUserId);

    /**
     * 获取某个用户的所有未读消息
     * @param id
     * @return
     */
    Integer getNoReadCount(Long id);

    /**
     * 用户读取和某个用户的消息(更新未读数量)
     * @param readUserId
     * @param sendUserId
     * @param readCount
     * @return
     */
    Boolean readMessage(Long readUserId, Long sendUserId, Integer readCount);

    /**
     * 获取某个用户的所有信息(顺序!)
     * @param id
     * @return
     */
    List<ReturnMsgVO> getAllMsg(Long id);
}
