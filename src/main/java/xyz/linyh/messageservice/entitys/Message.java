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
     * 会话id(由发送id和接收者id组成) 如果sessionId为-1 ，那么就是系统广播消息
     */
    private Long sessionId;


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
