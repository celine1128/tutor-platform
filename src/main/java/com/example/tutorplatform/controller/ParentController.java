package com.example.tutorplatform.controller;

import com.example.tutorplatform.entity.Demand;
import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private DemandService demandService;

    @GetMapping("/demand/form")
    public String showDemandForm() {
        return "parent/demand_form";
    }

    @PostMapping("/demand/publish")
    public String publishDemand(Demand demand, HttpSession session) {
        User user = (User) session.getAttribute("user");
        demand.setParentId(user.getId());
        demandService.publish(demand);
        return "redirect:/parent/demand/list";
    }

    @GetMapping("/demand/list")
    public String myDemands(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("demands", demandService.getByParentId(user.getId()));
        return "parent/my_demands";
    }
}