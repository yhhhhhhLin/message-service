package xyz.linyh.messageservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.messageservice.entitys.User;
import xyz.linyh.messageservice.model.BaseResponse;
import xyz.linyh.messageservice.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user, HttpServletRequest request) {
        Long id = null;
        if("张三".equals(user.getPassword())){
            id = 1L;
        }else if("李四".equals(user.getPassword())){
            id = 2L;
        }else {
            id = 3L;
        }
        HttpSession session = request.getSession();
        session.setAttribute("id", id);
        return ResultUtils.success(id);
    }
}
