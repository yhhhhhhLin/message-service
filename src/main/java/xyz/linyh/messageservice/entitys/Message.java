package xyz.linyh.messageservice.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    @Id
    private String id;
    /**
     * 会话表id
     */
    private Long contactId;

    /**
     * 发送人id
     */
    private Long fromUserId;

    /**
     * 消息具体内容
     */
    private String content;

    /**
     * 回复的消息内容
     */
    private Short replyMsgId;

    /**
     * 消息状态(0为正常，1为删除)
     */
    private Integer status;

    /**
     * 与回复的消息之间查多少天
     */
    private Date gapCount;

    /**
     * 消息类型（1为正常消息，0为撤回消息）
     */
    private Integer type;

    /**
     * 扩展消息
     */
    private String extra;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
