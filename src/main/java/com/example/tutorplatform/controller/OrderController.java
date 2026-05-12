package com.example.tutorplatform.controller;

import com.example.tutorplatform.entity.Demand;
import com.example.tutorplatform.entity.Order;
import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.service.DemandService;
import com.example.tutorplatform.service.OrderService;
import com.example.tutorplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DemandService demandService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String orderList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Order> orders;

        // 1. 根据角色获取对应的订单列表
        if ("parent".equals(user.getRole())) {
            orders = orderService.getOrdersByParentId(user.getId());
        } else {
            orders = orderService.getOrdersByTeacherId(user.getId());
        }

        // 2. 准备数据字典映射
        Map<Long, Demand> demandMap = new HashMap<>();
        Map<Long, String> userNicknameMap = new HashMap<>();

        for (Order order : orders) {
            // 修复点：Key 必须使用 order.getDemandId()，与 HTML 中的索引逻辑对齐
            Demand demand = demandService.getById(order.getDemandId());
            if (demand != null) {
                demandMap.put(order.getDemandId(), demand);
            }

            // 查询家长昵称（仅当 Map 中不存在时查询，减少数据库压力）
            if (order.getParentId() != null && !userNicknameMap.containsKey(order.getParentId())) {
                User parent = userService.getById(order.getParentId());
                userNicknameMap.put(order.getParentId(), parent != null ? parent.getNickname() : "未知家长");
            }

            // 查询老师昵称
            if (order.getTeacherId() != null && !userNicknameMap.containsKey(order.getTeacherId())) {
                User teacher = userService.getById(order.getTeacherId());
                userNicknameMap.put(order.getTeacherId(), teacher != null ? teacher.getNickname() : "未知教员");
            }
        }

        // 3. 数据推送到前端
        model.addAttribute("orders", orders);
        model.addAttribute("demandMap", demandMap);
        model.addAttribute("userNicknameMap", userNicknameMap);
        model.addAttribute("currentRole", user.getRole());

        return "order/order_list";
    }

    @PostMapping("/complete")
    public String completeOrder(@RequestParam Long orderId) {
        orderService.completeOrder(orderId);
        return "redirect:/order/list";
    }
}