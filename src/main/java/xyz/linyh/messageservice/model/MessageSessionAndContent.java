package xyz.linyh.messageservice.model;

import lombok.Data;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.entitys.MessageSession;

import java.util.List;

@Data
public class MessageSessionAndContent extends MessageSession {

    private String messageContent;

    private Integer unReadCount;


}
