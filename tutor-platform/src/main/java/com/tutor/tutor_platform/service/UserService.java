package com.tutor.tutor_platform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tutor.tutor_platform.entity.User;
import com.tutor.tutor_platform.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        
        // 使用 BCrypt 加密密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));
        
        user.setRole(role);
        user.setNickname(nickname);
        
        return this.save(user);
    }
    
    // 登录验证
    public User login(String phone, String password) {
        // 根据手机号查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = this.getOne(wrapper);
        
        if (user == null) {
            return null; // 用户不存在
        }
        
        // 验证密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
            return user; // 密码正确
        }
        
        return null; // 密码错误
    }
}