package com.example.tutorplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.tutorplatform.entity.Demand;
import com.example.tutorplatform.mapper.DemandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public List<Demand> getAvailable() {
        LambdaQueryWrapper<Demand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Demand::getStatus, 0)
                .orderByDesc(Demand::getCreateTime);
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
}