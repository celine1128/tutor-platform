package com.example.tutorplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tutorplatform.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 订单模块 Mapper 接口
 * 继承 BaseMapper 后，无需编写 SQL 即可实现对 tutor_order 表的基础操作
 */
@Mapper
@Repository // 添加该注解可以消除 Service 层注入时可能出现的 IDEA 红色波浪线警告
public interface OrderMapper extends BaseMapper<Order> {

}