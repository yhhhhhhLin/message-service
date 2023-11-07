package xyz.linyh.messageservice.model;
import lombok.Data;

@Data
public class MessageVO {

    private Long sessionId;

    private String message;

    private Long fromUserId;

    private Long toUserId;
}
