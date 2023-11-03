package xyz.linyh.messageservice.model;

import lombok.Data;

@Data
public class MessageVO {

    private String message;

    private Long fromUserId;

    private Long toUserId;
}
