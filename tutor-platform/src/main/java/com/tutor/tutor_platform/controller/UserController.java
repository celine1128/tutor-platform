package com.tutor.tutor_platform.controller;

import com.tutor.tutor_platform.entity.User;
import com.tutor.tutor_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * 访问地址: POST http://localhost:8080/api/user/register
     * 请求体示例: {"phone":"13800138000","password":"123456","role":"STUDENT","nickname":"张三"}
     */
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        boolean success = userService.register(
            user.getPhone(),
            user.getPassword(),
            user.getRole(),
            user.getNickname()
        );
        
        if (success) {
            return "注册成功！";
        } else {
            return "注册失败，手机号已存在！";
        }
    }
    
    /**
     * 用户登录接口
     * 访问地址: POST http://localhost:8080/api/user/login
     * 请求体示例: {"phone":"13800138000","password":"123456"}
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginInfo) {
        String phone = loginInfo.get("phone");
        String password = loginInfo.get("password");
        
        User user = userService.login(phone, password);
        
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("success", true);
            result.put("message", "登录成功！");
            result.put("userId", user.getId());
            result.put("phone", user.getPhone());
            result.put("role", user.getRole());
            result.put("nickname", user.getNickname());
        } else {
            result.put("success", false);
            result.put("message", "登录失败，手机号或密码错误！");
        }
        return result;
    }
}