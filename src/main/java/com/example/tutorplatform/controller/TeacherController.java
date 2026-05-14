package com.example.tutorplatform.controller;

import com.example.tutorplatform.entity.Demand;
import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.service.DemandService;
import com.example.tutorplatform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private DemandService demandService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/available")
    public String getAvailableDemands(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            Model model) {

        // 将参数传递给 Service 层进行筛选
        // 在 TeacherController.java 中
        List<Demand> demands = demandService.findFilteredDemands(subject, grade, minPrice, maxPrice);
        model.addAttribute("demands", demands);

        // 同时也把筛选条件传回页面，方便在搜索框中回显
        model.addAttribute("subject", subject);
        model.addAttribute("grade", grade);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "teacher/available_demands";
    }

    @GetMapping("/demands")
    public String demands() {
        return "redirect:/teacher/available";
    }

    @PostMapping("/accept")
    public String acceptDemand(@RequestParam Long demandId, HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        Demand demand = demandService.getById(demandId);
        if (demand != null && demand.getStatus() == 0) {
            boolean ok = orderService.acceptDemand(demandId, teacher.getId(), demand.getParentId());
            if (!ok) {
                return "redirect:/teacher/available?error=已被接单";
            }
        }
        return "redirect:/order/list";
    }
}