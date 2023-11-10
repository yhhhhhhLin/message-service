package xyz.linyh.messageservice.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 房间表（可以是个人聊天，也可以是群聊）
 * @author lin
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {
    /**
     * 房间id
     */
    private Long id;

    /**
     * 房间类型（1群聊，2单聊）
     */
    private Integer type;

    /**
     * 是否全员展示（0否 1是）
     */
    private Integer hotFlag;

    /**
     * 最后活跃时间
     */
    private Date activeTime;

    /**
     * 会话中最后一条消息id
     */
    private String lastMsgId;

    /**
     * 额外消息
     */
    private String extJson;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
