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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private DemandService demandService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/available")
    public String availableDemands(Model model) {
        model.addAttribute("demands", demandService.getAvailable());
        return "teacher/available_demands";
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