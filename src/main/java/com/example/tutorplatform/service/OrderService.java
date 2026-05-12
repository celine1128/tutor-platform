package com.example.tutorplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.tutorplatform.entity.Order;
import com.example.tutorplatform.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DemandService demandService;

    // 老师接单：创建订单，修改需求状态为已接单
    @Transactional
    public boolean acceptDemand(Long demandId, Long teacherId, Long parentId) {
        if (demandService.getById(demandId).getStatus() != 0) {
            return false;
        }
        Order order = new Order();
        order.setDemandId(demandId);
        order.setTeacherId(teacherId);
        order.setParentId(parentId);
        order.setStatus(0);
        orderMapper.insert(order);
        demandService.updateStatus(demandId, 1);
        return true;
    }

    // 家长确认完成
    @Transactional
    public boolean completeOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(1);
            order.setCompleteTime(LocalDateTime.now());
            return orderMapper.updateById(order) > 0;
        }
        return false;
    }

    // 查询家长收到的订单
    public List<Order> getOrdersByParentId(Long parentId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getParentId, parentId)
                .orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(wrapper);
    }

    // 查询老师接的订单
    public List<Order> getOrdersByTeacherId(Long teacherId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getTeacherId, teacherId)
                .orderByDesc(Order::getCreateTime);
        return orderMapper.selectList(wrapper);
    }

    public Order getById(Long id) {
        return orderMapper.selectById(id);
    }
}