package xyz.linyh.messageservice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.linyh.messageservice.entitys.Contact;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsVO extends Contact {

    /**
     * 未读消息
     */
    private Long unReadCount;

    /**
     * 用户名字
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;

}
