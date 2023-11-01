package xyz.linyh.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.linyh.messageservice.entitys.Message;

import java.util.List;

/**
 * 用来返回某个用户有的消息
 * @author lin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMsgVO {


    /**
     * 如果id为0，那么就是系统消息
     */
    private Long sendUserId;

    private List<Message> messages;

    /**
     * 未读的消息数量
     */
    private Integer UnreadMessage;
}
