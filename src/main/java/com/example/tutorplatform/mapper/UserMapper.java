package com.example.tutorplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tutorplatform.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}