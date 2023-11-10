package xyz.linyh.messageservice.model;

import lombok.Data;

import java.util.Date;

/**
 * 获取用户直接历史聊天记录的请求包装类
 */

@Data
public class GetMsgHistoryDTO {

    private Integer msgSize;

    /**
     * 从哪个时间开始往前的消息
     */
    private Date endDate;

    /**
     * 和谁之间的聊天消息
     */
    private Long fromUserId;


}
