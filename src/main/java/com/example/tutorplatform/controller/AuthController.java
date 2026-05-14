package com.example.tutorplatform.controller;

import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.service.DemandService;
import com.example.tutorplatform.service.OrderService;
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

    @Autowired
    private DemandService demandService;

    @Autowired
    private OrderService orderService;

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

        // --- 修改此处逻辑 ---
        // 不再根据角色跳转到特定页面，而是直接重定向到根路径（首页/我的看板）
        return "redirect:/";
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // 全局统计数据
        long teacherCount = userService.countTeachers();
        long demandCount = demandService.countAvailableDemands();
        model.addAttribute("teacherCount", teacherCount);
        model.addAttribute("demandCount", demandCount);

        // 个人看板数据
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if ("parent".equals(user.getRole())) {
                model.addAttribute("myDemandCount", demandService.countDemandsByParent(user.getId()));
                model.addAttribute("myFinishCount", orderService.countCompletedOrders(user.getId(), "parent"));
            } else if ("teacher".equals(user.getRole())) {
                model.addAttribute("myOrderCount", orderService.countOrdersByTeacher(user.getId()));
                model.addAttribute("myFinishCount", orderService.countCompletedOrders(user.getId(), "teacher"));
            }
        }

        return "index";
    }
}