package xyz.linyh.messageservice.entitys;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class MessageSession {

     @Id
    private Long id;

    /**
     * 接收人id
     */
    private Long toUserId;

    /**
     * 发送人id（如果id为0，那么就是系统消息）
     */
    private Long fromUserId;

    /**
     * 是否置顶会话
     */
    private Boolean setTop;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
