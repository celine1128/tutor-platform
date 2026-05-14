package com.example.tutorplatform.controller;

import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.service.DemandService;
import com.example.tutorplatform.service.OrderService; // 1. 导入 OrderService [cite: 11]
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
    private OrderService orderService; // 2. 注入 OrderService 用于统计个人看板数据 [cite: 11]

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
        // 根据角色跳转到不同页面
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * 首页逻辑：包含全局统计和个人看板统计
     */
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // 1. 全局统计数据：所有人可见
        long teacherCount = userService.countTeachers();
        long demandCount = demandService.countAvailableDemands();
        model.addAttribute("teacherCount", teacherCount);
        model.addAttribute("demandCount", demandCount);

        // 2. 个人看板数据：仅登录后可见
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if ("parent".equals(user.getRole())) {
                // 家长端：统计发布的总需求和已完成的订单
                model.addAttribute("myDemandCount", demandService.countDemandsByParent(user.getId()));
                model.addAttribute("myFinishCount", orderService.countCompletedOrders(user.getId(), "parent"));
            } else if ("teacher".equals(user.getRole())) {
                // 老师端：统计接单总数和已完成的订单
                model.addAttribute("myOrderCount", orderService.countOrdersByTeacher(user.getId()));
                model.addAttribute("myFinishCount", orderService.countCompletedOrders(user.getId(), "teacher"));
            }
        }

        return "index"; // 返回 index.html [cite: 13]
    }
}