package xyz.linyh.messageservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.messageservice.constant.ErrorCode;
import xyz.linyh.messageservice.entitys.Message;
import xyz.linyh.messageservice.model.*;
import xyz.linyh.messageservice.service.MessageService;
import xyz.linyh.messageservice.utils.ResultUtils;
import xyz.linyh.messageservice.ws.MessageEndpoint;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/msg")
public class MessageController {


    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageEndpoint messageEndpoint;

    @GetMapping("/unreadcount")
    public BaseResponse<ReturnMsgVO> GetNoReadCount(HttpServletRequest request){
        Long userId = (Long)request.getAttribute("id");
        Integer unReadCount = messageService.getNoReadCount(userId);

        return ResultUtils.success(new ReturnMsgVO(unReadCount));
    }

    /**
     * 发送消息给所有用户，如果是在线用户，那么就实时发送
     * @param messageVO
     * @return
     */
    @PostMapping("/broadcast")
    public BaseResponse<ReturnMsgVO> BroadcastAllMsg(@RequestBody MessageVO messageVO){

        if(messageVO==null || messageVO.getMessage()==null || messageVO.getToUserId()==null || messageVO.getFromUserId()==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
//        将数据发送给在线用户
        messageEndpoint.broadcastAllUser(messageVO.getMessage(),messageVO.getFromUserId());
//        获取所有数据保存到数据库 todo 获取所有用户id rpc调用
        List<Long> ids = new ArrayList<>();
        ids.add(3L);
        messageService.saveBroadcast(ids,messageVO.getMessage());

        return ResultUtils.success(null);
    }

    /**
     * 发送消息，将消息保存到session和对应的消息表里面
     * @param messageVO
     * @return
     */

    @PostMapping("/send")
    public BaseResponse sendMessage(@RequestBody MessageVO messageVO){

        if(messageVO==null || messageVO.getMessage()==null || messageVO.getToUserId()==null || messageVO.getFromUserId()==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
//        判断用户是否在线
        Session online = messageEndpoint.isOnline(messageVO.getToUserId());

        if(online!=null){
//            发送给在线用户
            messageEndpoint.sendMessageToUser(messageVO.getFromUserId(),messageVO.getToUserId(),messageVO.getMessage());
        }
//        保存到数据库
        messageService.addOne(messageVO.getSessionId(), messageVO.getMessage());
        return ResultUtils.success(null);
    }

//    todo 前端可以判断用户是否在那个聊天页面,然后更新读取的消息数量
    /**
     * 消息标为已读
     * @return
     */
    @PostMapping("/read")
    public BaseResponse readMessage(@RequestBody ReadMessageVO readMessageVO){

        if(readMessageVO==null || readMessageVO.getReadUserId()==null || readMessageVO.getSendUserId()==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        Boolean b = messageService.readMessage(readMessageVO.getReadUserId(),readMessageVO.getSendUserId(),readMessageVO.getReadCount());

        if(b){
            return ResultUtils.success(null);
        }else{
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        }

    }

    /**
     * 获取这个用户的所有未读消息（前端获取后需要把数据保存到storage中，后面如果用户在线了，发送消息，那么就把数据继续更新）
     * @return
     */
    @GetMapping("/getunreadmsg")
    public BaseResponse<List<List<Message>>> getAllMsg(HttpServletRequest request){

        Long id = (Long) request.getSession().getAttribute("id");
//        todo
        List<List<Message>> allMsg = messageService.getAllMsg(id);

        return ResultUtils.success(allMsg);
    }

    /**
     * 获取和某个用户之间的所有消息
      * @param fromUserId
     * @return
     */
    @GetMapping("/getUsermsg")
    public BaseResponse<ReturnMsgVO> getUserMessage(Long fromUserId,HttpServletRequest request){

        if(fromUserId==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        Long userId = (Long) request.getAttribute("id");

        ReturnMsgVO msg = messageService.getMsg(userId, fromUserId);

        return ResultUtils.success(msg);
    }

    /**
     * 获取所有消息会话，返回用户用户名称，用户头像，最新的一条消息，未读消息数量
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/hisrotySession")
    public BaseResponse getHistorySession(Integer page,Integer pageSize,HttpServletRequest request){
//        todo 参数校验

        Long id = (Long) request.getSession().getAttribute("id");

//        按照时间倒叙查找对应的最新聊天，找到对应的最新聊天未读消息数量和对应的用户名字
        List<MessageSessionAndContent> historySession = messageService.getHistorySession(id, page, pageSize);

        return ResultUtils.success(historySession);
    }

    /**
     * 获取和某个用户之间的聊天记录，按照时间顺序返回所有查询出来的聊天（有发送时间，消息内容,谁发送的和对应id信息）
     * @param historyDTO
     * @return
     */
    @GetMapping("/history")
    public BaseResponse getHistory(@RequestBody GetMsgHistoryDTO historyDTO,HttpServletRequest request){

        return null;
    }

    /**
     * 将和某个用户之间的未读消息全部清除
     * @param fromUserId
     * @param request
     * @return
     */
    @GetMapping("/clean")
    public BaseResponse cleanMsg(Long fromUserId,HttpServletRequest request){


        return null;
    }

    /**
     * 一键已读
     * @return
     */
    @PostMapping("/clean")
    public BaseResponse cleanAllMsg(HttpServletRequest request){

        return null;
    }





}
