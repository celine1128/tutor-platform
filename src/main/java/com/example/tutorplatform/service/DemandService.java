package com.example.tutorplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.tutorplatform.entity.Demand;
import com.example.tutorplatform.mapper.DemandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.util.StringUtils;

@Service
public class DemandService {

    @Autowired
    private DemandMapper demandMapper;

    // 发布需求
    public void publish(Demand demand) {
        demand.setStatus(0);
        demandMapper.insert(demand);
    }

    // 查询家长自己的需求
    public List<Demand> getByParentId(Long parentId) {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getParentId, parentId)
                .orderByDesc(Demand::getCreateTime);
        return demandMapper.selectList(wrapper);
    }

    // 查询所有待接单需求
    public List<Demand> findFilteredDemands(String subject, String grade, Integer minPrice, Integer maxPrice) {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();

        // 1. 基础条件：必须是待接单状态 (status = 0)
        wrapper.eq(Demand::getStatus, 0);

        // 2. 动态筛选条件
        // 科目：使用模糊查询 (like)
        wrapper.like(StringUtils.hasText(subject), Demand::getSubject, subject);

        // 年级：使用精确匹配 (eq)
        wrapper.eq(StringUtils.hasText(grade), Demand::getGrade, grade);

        // 价格区间：使用 ge (大于等于) 和 le (小于等于)
        wrapper.ge(minPrice != null, Demand::getPrice, minPrice);
        wrapper.le(maxPrice != null, Demand::getPrice, maxPrice);

        // 3. 排序：按创建时间倒序
        wrapper.orderByDesc(Demand::getCreateTime);

        return demandMapper.selectList(wrapper);
    }

    // 根据ID查询需求
    public Demand getById(Long id) {
        return demandMapper.selectById(id);
    }

    // 更新需求状态
    public boolean updateStatus(Long demandId, Integer status) {
        Demand demand = demandMapper.selectById(demandId);
        if (demand != null) {
            demand.setStatus(status);
            return demandMapper.updateById(demand) > 0;
        }
        return false;
    }
    // 统计待接单的需求总数
    public long countAvailableDemands() {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getStatus, 0);
        return demandMapper.selectCount(wrapper);
    }
    // 统计家长发布的需求总数
    public long countDemandsByParent(Long parentId) {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getParentId, parentId);
        return demandMapper.selectCount(wrapper);
    }
}
