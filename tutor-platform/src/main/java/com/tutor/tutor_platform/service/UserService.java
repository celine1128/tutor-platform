package com.tutor.tutor_platform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tutor.tutor_platform.entity.User;
import com.tutor.tutor_platform.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    // 注册
    public boolean register(String phone, String password, String role, String nickname) {
        // 检查手机号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        if (this.count(wrapper) > 0) {
            return false;
        }
        
        // 创建新用户
        User user = new User();
        user.setPhone(phone);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setRole(role);
        user.setNickname(nickname);
        
        return this.save(user);
    }
}