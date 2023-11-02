package xyz.linyh.messageservice.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应mongodb里面的消息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    @Id
    private String id;
    /**
     * 接收人id
     */
    private Long toUserId;

    /**
     * 发送人id（如果id为0，那么就是系统消息）
     */
    private Long fromUserId;

    /**
     * 消息具体内容
     */
    private String msgContent;

    /**
     * 消息是否已读
     */
    private Short isRead;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
