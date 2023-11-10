package xyz.linyh.messageservice.dao;


import xyz.linyh.messageservice.entitys.Message;

import java.util.List;

public interface MessageDao {

    /**
     * 单个插入
     * @param message
     */
    void addOne(Message message);

    /**
     * 根据id获取
     * @param messageId
     */
    void deleteOneById(String  messageId);

    /**
     * 对某一个值进行修改
     * @param message
     */
    void updateOne(Message message);

    /**
     * 根据id修改对应数据
     * @param messageId
     * @return
     */
    Message findOneById(String messageId);

    /**
     * 获取全部数据
     * @return
     */
    List<Message> findAll();
}
