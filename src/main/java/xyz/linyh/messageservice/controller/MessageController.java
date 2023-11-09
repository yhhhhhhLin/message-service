package xyz.linyh.messageservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.messageservice.entitys.Contact;
import xyz.linyh.messageservice.model.*;
import xyz.linyh.messageservice.model.vo.ContactsVO;
import xyz.linyh.messageservice.service.MessageService;
import xyz.linyh.messageservice.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 消息controller
 */
@RestController
@RequestMapping("/msg")
public class MessageController {


    @Autowired
    private MessageService messageService;


    /**
     * 获取用户消息列表,返回的userId1为对方用户的id
     * @return
     */
    @GetMapping("/sessionPage")
    public BaseResponse<List<ContactsVO>> getMsgSessionPage(HttpServletRequest request,Integer pageSize,Integer currentPage){
        Long userId = getUserId(request);
        List<ContactsVO> sessions = messageService.getSessionByPage(userId,pageSize,currentPage);

//        最后需要调用user服务添加对应用户头像和用户名字和未读消息数量
        return ResultUtils.success(sessions);
    }

    private Long getUserId(HttpServletRequest request) {
        return 1L;
//        return (Long) request.getSession().getAttribute("id");
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

   @GetMapping("/getUnreadMsg")
    public BaseResponse getUnreadMsg(){
       ThreadLocal<Long> longThreadLocal = new ThreadLocal<>();
//       通过上面获取对应id
       Long id = 1L;
       Long unreadCount = messageService.getUnreadCount(id);
        return null;
    }
}
