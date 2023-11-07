package xyz.linyh.messageservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.messageservice.model.*;
import xyz.linyh.messageservice.service.MessageService;

import javax.servlet.http.HttpServletRequest;


/**
 * 消息controller
 */
@RestController
@RequestMapping("/msg")
public class MessageController {


    @Autowired
    private MessageService messageService;


    /**
     * 获取用户消息列表
     * @return
     */
    @GetMapping("/msg/sessionPage")
    public BaseResponse<Object> getMsgSessionPage(HttpServletRequest request,Integer pageSize,Integer currentPage){

        return null;
    }

    /**
     * 找出对应会话的所有消息
     * 思路：根据会话id找到对应的消息,按照时间顺序发送到前端，前端遍历的时候根据fromId来判断消息是在左边还是右边
     * @param request
     * @param pageSize
     * @param currentPage
     * @return
     */
    @GetMapping
    public BaseResponse getMsg(HttpServletRequest request,Integer pageSize,Integer currentPage){

        return null;
    }

    public BaseResponse sendMsg(){
        return null;
    }
}
