package com.tutor.tutor_platform.controller;

import com.tutor.tutor_platform.entity.User;
import com.tutor.tutor_platform.service.UserService;
import jakarta.servlet.http.HttpSession;
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
     * 用户登录接口（保存 Session）
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginInfo, HttpSession session) {
        String phone = loginInfo.get("phone");
        String password = loginInfo.get("password");
        
        User user = userService.login(phone, password);
        
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            // 登录成功，将用户信息存入 Session
            session.setAttribute("loginUser", user);
            
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
    
    /**
     * 获取当前登录用户信息（需要登录）
     */
    @GetMapping("/currentUser")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("success", true);
            result.put("userId", user.getId());
            result.put("phone", user.getPhone());
            result.put("role", user.getRole());
            result.put("nickname", user.getNickname());
        } else {
            result.put("success", false);
            result.put("message", "未登录");
        }
        return result;
    }
    
    /**
     * 退出登录（清除 Session）
     */
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.removeAttribute("loginUser");
        session.invalidate();  // 使 Session 失效
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "退出成功！");
        return result;
    }
}