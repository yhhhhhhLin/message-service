package xyz.linyh.messageservice.controller;

import org.springframework.web.bind.annotation.*;
import xyz.linyh.messageservice.constant.ErrorCode;
import xyz.linyh.messageservice.entitys.User;
import xyz.linyh.messageservice.model.BaseResponse;
import xyz.linyh.messageservice.model.dto.LoginDto;
import xyz.linyh.messageservice.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user, HttpServletRequest request) {
        Long id = null;
        if("张三".equals(user.getUsername())){
            id = 1L;
        }else if("李四".equals(user.getUsername())){
            id = 2L;
        }else {
            id = 3L;
        }
        HttpSession session = request.getSession();
        session.setAttribute("id", id);
        return ResultUtils.success(id);
    }

    @PostMapping("/user/login")
    public BaseResponse userLogin(@RequestBody LoginDto dto,HttpServletRequest request){
        if("admin".equals(dto.getUsername()) && "admin".equals(dto.getPassword())){
            request.getSession().setAttribute("id","1");
            HashMap<String, String> map = new HashMap<>();
            map.put("token","12345");
            return ResultUtils.success(map);
        }
        return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
    }
}
