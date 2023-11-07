package xyz.linyh.messageservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.messageservice.entitys.Contact;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.model.ReturnMsgVO;
import xyz.linyh.messageservice.service.MessageService;

import java.util.ArrayList;
import java.util.Date;

@RestController
public class TestController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/test")
    public void test(){
//        创建相应的contact和对应的message信息
        Long userId = 1L;
        Long toUserId = 2L;
        messageService.sendMsg(userId, toUserId, "test");
    }

    @GetMapping("/test2")
    public void test2(){
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(6L);
        ids.add(7L);
        ids.add(8L);
        ids.add(9L);
//        messageService.saveBroadcast(ids,"系统消息发送测试1");
    }

    @GetMapping("/test3")
    public void test3ds(){
//        ReturnMsgVO msg = messageService.getMsg(1L, 0L);
//        System.out.println(msg);
    }
}
