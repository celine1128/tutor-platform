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

    // 家长确认完成：只有该订单对应的家长可以确认完成
    @Transactional
    public boolean completeOrder(Long orderId, Long parentId) {
        Order order = orderMapper.selectById(orderId);

        if (order == null) {
            return false;
        }

        // 只能确认自己的订单
        if (!order.getParentId().equals(parentId)) {
            return false;
        }

        // 只有待确认订单可以完成
        if (order.getStatus() != 0) {
            return false;
        }

        order.setStatus(1);
        order.setCompleteTime(LocalDateTime.now());
        return orderMapper.updateById(order) > 0;
    }
    // 取消订单：家长或老师都可以取消待确认订单，取消后需求重新回到待接单状态
    @Transactional
    public boolean cancelOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);

        if (order == null) {
            return false;
        }

        // 只有待确认订单可以取消
        if (order.getStatus() != 0) {
            return false;
        }

        // 只能由订单相关的家长或老师取消
        if (!order.getParentId().equals(userId) && !order.getTeacherId().equals(userId)) {
            return false;
        }

        order.setStatus(2);
        boolean updated = orderMapper.updateById(order) > 0;

        // 订单取消后，让原需求重新回到待接单状态
        if (updated) {
            demandService.updateStatus(order.getDemandId(), 0);
        }

        return updated;
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
    // 统计老师接单总数
    public long countOrdersByTeacher(Long teacherId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getTeacherId, teacherId);
        return orderMapper.selectCount(wrapper);
    }

    // 统计订单完成总数 (状态为1)
    public long countCompletedOrders(Long userId, String role) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if ("teacher".equals(role)) {
            wrapper.eq(Order::getTeacherId, userId);
        } else {
            wrapper.eq(Order::getParentId, userId);
        }
        wrapper.eq(Order::getStatus, 1);
        return orderMapper.selectCount(wrapper);
    }
}