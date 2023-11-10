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
    private String contactId;

    /**
     * 发送人id
     */
    private Long fromUserId;

    /**
     * 消息具体内容
     */
    private String content;

    /**
     * 回复的消息内容Id(0就是没有)
     */
    private String replyMsgId;

    /**
     * 消息状态(0为正常，1为删除)
     */
    private Integer status;

    /**
     * 与上一个回复之间查多少条消息
     */
    private Integer gapCount;

    /**
     * 是否已读，0为未读 1为已读
     */
    private Integer isRead;

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
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

}
