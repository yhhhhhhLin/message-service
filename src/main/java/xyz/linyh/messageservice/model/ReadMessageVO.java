package xyz.linyh.messageservice.model;

import lombok.Data;

@Data
public class ReadMessageVO {

    /**
     * 读取消息用户的id
     */
    private Long readUserId;

    /**
     * 发送消息的用户id
     */
    private Long sendUserId;

    /**
     * 读取消息的数量
     */
    private Integer readCount;
}
