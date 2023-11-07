package xyz.linyh.messageservice.ws;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import xyz.linyh.messageservice.config.GetHttpSessionConfig;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.model.BaseResponse;
import xyz.linyh.messageservice.model.ReturnMsgVO;
import xyz.linyh.messageservice.service.MessageService;
import xyz.linyh.messageservice.utils.ResultUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lin
 */
@ServerEndpoint(value = "/message",configurator = GetHttpSessionConfig.class)
@Slf4j
@Controller
public class MessageEndpoint {

    @Resource
    public MessageService messageService;

    /**
     * 在线人数
     */
    private static final ConcurrentHashMap<Long,Session> ONLINE_PEOPLE = new ConcurrentHashMap<>();

    private HttpSession httpSession;


    /**
     * 连接操作
     * @param session
     */

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        this.httpSession = (HttpSession)config.getUserProperties().get("httpSession");

//        获取用户唯一表示
        Long id = (Long) httpSession.getAttribute("id");
//        存储到map中
        ONLINE_PEOPLE.put(id,session);

        log.info("有用户登录在线成功");
    }


    /**
     * 发送消息
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        Message msg = JSON.parseObject(message, Message.class);

   }

    /**
     * 断开连接操作
     * @param
     */
    @OnClose
    public void onClose(){
        Long userId = (Long) httpSession.getAttribute("id");
        ONLINE_PEOPLE.remove(userId);
        log.info("有用户退出");
    }
}
