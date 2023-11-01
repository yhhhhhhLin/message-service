package xyz.linyh.messageservice.service;

import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.model.ReturnMsgVO;

import java.util.List;

public interface MessageService {
    public void addOne(Message message);

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
     * 发送系统统一消息
     */
    public void broadcastAllUsers(List<Long> ids, String message);

    /**
     * 获取
     * @param userId
     * @param sendUserId 如果为0，那么就是系统通知
     * @return
     */
    public ReturnMsgVO getMsg(Long userId, Long sendUserId);
}
