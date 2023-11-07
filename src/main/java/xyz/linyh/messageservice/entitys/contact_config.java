package xyz.linyh.messageservice.entitys;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class contact_config {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 对应会话id
     */
    private Long contactId;

    /**
     * 状态（0为正常 1为删除）
     */
    private Integer status;

    /**
     * 会话是否置顶 0为否 1为是
     */
    private Integer setTop;

    private Date createTime;

    private Date updateTime;
}
