package xyz.linyh.messageservice.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话列表
 * @author lin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact implements Serializable {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 用户1对应id
     */
    private Long userId1;

    /**
     * 用户2对应id
     */
    private Long userId2;


    /**
     * 最后活跃时间
     */
    private Long activeTime;

    /**
     * 最新消息id
     */
    private String lastMsgId;

    /**
     * 是否置顶(0为否 1为是)
     */
    private Integer setTop;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
