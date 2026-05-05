package com.tutor.tutor_platform.config;

import com.tutor.tutor_platform.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 Session
        HttpSession session = request.getSession();
        
        // 从 Session 中获取用户信息
        User user = (User) session.getAttribute("loginUser");
        
        // 判断是否已登录
        if (user == null) {
            // 未登录，返回 401 未授权状态码
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":false,\"message\":\"请先登录\"}");
            return false;
        }
        
        // 已登录，放行
        return true;
    }
}