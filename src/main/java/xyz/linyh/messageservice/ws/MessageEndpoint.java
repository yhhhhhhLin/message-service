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
    private static final ConcurrentHashMap<Long,Session> onlinePeople = new ConcurrentHashMap<>();

    private HttpSession httpSession;

    /**
     * 连接操作
     * @param session
     */

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        HttpSession httpSession1 = (HttpSession)config.getUserProperties().get("httpSession");
        this.httpSession=httpSession1;
//        获取用户唯一表示
        Long id = (Long) httpSession.getAttribute("id");
        onlinePeople.put(id,session);

//        Integer noReadCount = messageService.getNoReadCount(id);
//        返回给前端还有多少未读数量
//        BaseResponse<ReturnMsgVO> success = ResultUtils.success(new ReturnMsgVO(noReadCount));
//        try {
////            todo 需要前端保存到storage
//            session.getBasicRemote().sendText(JSON.toJSONString(success));
//        } catch (IOException e) {
//            log.info("消息发送失败");
//        }
//        存储到map中
        log.info("有用户登录在线成功");
    }


    /**
     * 发送消息
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        Message msg = JSON.parseObject(message, Message.class);

//        系统关播消息，
        if(msg.getFromUserId()==0){
//            将消息保存到数据库中
//            todo 获取所有用户id
            List<Long> ids = new ArrayList<>();
//            messageService.saveBroadcast(ids,msg.getMsgContent());
//           将增加未读消息这件事发送给前端,前端如果在消息查看这个页面,那么就可以重新获取消息,如果不是那么就会增加消息的未读数量
//            broadcastAllUser(msg.getMsgContent(), (Long) httpSession.getAttribute("id"));
        }

//        如果不是关播，就需要点对点发送给用户
//        谁发送的
//        Long fromUserId = msg.getFromUserId();
////        发送给谁的
//        Long toUserId = msg.getToUserId();
//
//        Session online = isOnline(toUserId);
//        if(online!=null){
//            ReturnMsgVO returnMsgVO = new ReturnMsgVO(fromUserId,null, 1);
//            String respMsg = JSON.toJSONString(ResultUtils.success(returnMsgVO));
//            try {
//                online.getBasicRemote().sendText(respMsg);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
////        保存到数据库
//        Message saveMsg = new Message();
//        saveMsg.setToUserId(toUserId);
//        saveMsg.setFromUserId(fromUserId);
//        saveMsg.setMsgContent(msg.getMsgContent());
//        saveMsg.setIsRead((short) 0);
//        saveMsg.setCreateTime(new Date());
//        saveMsg.setUpdateTime(new Date());
//        messageService.addOne(saveMsg);
    }

    /**
     * 判断某个用户是否在线
     * @param userId
     * @return
     */
    public Session isOnline(Long userId){
        for (Long id : onlinePeople.keySet()) {
            if(id.equals(userId)){
                onlinePeople.get(onlinePeople);
            }
        }
        return null;
    }

    /**
     * 将消息广播给所有在线用户
     */
    public void broadcastAllUser(String messageContent,Long userId) {
        log.info("有用户广播消息");
        for (Session session : onlinePeople.values()) {

//            发送给前端新增未读消息数量,todo 可能会出现并发问题
            Message message = new Message();
            message.setMsgContent(messageContent);
//            todo 前端需要实时更新未读消息数量，后面如果前端查看消息后，也需要实时更新未读消息数量
            ReturnMsgVO returnMsgVO = new ReturnMsgVO(0L, List.of(message), 1);
            String respMsg = JSON.toJSONString(ResultUtils.success(returnMsgVO));

            try {
                session.getBasicRemote().sendText(respMsg);
            } catch (IOException e) {
                log.error("发送消息失败");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 断开连接操作
     * @param
     */
    @OnClose
    public void onClose(){
        Long userId = (Long) httpSession.getAttribute("id");
        onlinePeople.remove(userId);
        log.info("有用户退出");
    }

    /**
     * 如果用户在线就实时发送消息给某个用户
     * @param fromUserId
     * @param toUserId
     * @param message
     * @return
     */
    public Boolean sendMessageToUser(Long fromUserId, Long toUserId, String message) {
        return null;
    }
}
