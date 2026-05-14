package com.example.tutorplatform.interceptor;

import com.example.tutorplatform.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        // 放行不需要登录的页面，防止 /login 或 /forgot-password 死循环
        if ("/".equals(uri)
                || "/login".equals(uri)
                || "/register".equals(uri)
                || "/forgot-password".equals(uri)
                || "/error".equals(uri)
                || "/favicon.ico".equals(uri)
                || uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/images/")) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}