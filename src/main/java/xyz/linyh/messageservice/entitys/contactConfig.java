package xyz.linyh.messageservice.entitys;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class contactConfig {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 对应会话id
     */
    private String contactId;

    /**
     * 状态（0为正常 1为删除）
     */
    private Integer status;

    /**
     * 会话是否置顶 0为否 1为是
     */
    private Integer setTop;


    private Long createTime;

    private Long updateTime;
}
