package com.example.tutorplatform.controller;

import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String phone,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userService.login(phone, password);
        if (user == null) {
            model.addAttribute("error", "手机号或密码错误");
            return "login";
        }
        session.setAttribute("user", user);
        if ("parent".equals(user.getRole())) {
            return "redirect:/parent/demand/form";
        } else {
            return "redirect:/teacher/available";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String role,
                           @RequestParam String nickname,
                           Model model) {
        boolean success = userService.register(phone, password, role, nickname);
        if (!success) {
            model.addAttribute("error", "手机号已存在");
            return "register";
        }
        return "redirect:/login";
    }
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String phone,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {

        if (phone == null || phone.trim().isEmpty()) {
            model.addAttribute("error", "手机号不能为空");
            return "forgot-password";
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            model.addAttribute("error", "新密码不能为空");
            return "forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "forgot-password";
        }

        boolean success = userService.resetPassword(phone, newPassword);

        if (!success) {
            model.addAttribute("error", "手机号不存在");
            return "forgot-password";
        }

        model.addAttribute("success", "密码重置成功，请重新登录");
        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}