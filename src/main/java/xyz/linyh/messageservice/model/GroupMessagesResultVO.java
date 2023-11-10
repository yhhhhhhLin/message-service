package xyz.linyh.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.linyh.messageservice.entitys.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessagesResultVO {
    private Message groupMessages;
}
