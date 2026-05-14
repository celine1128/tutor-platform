package com.example.tutorplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.mapper.UserMapper;
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
            return null;
        }

        // 验证密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    // 重置密码
    public boolean resetPassword(String phone, String newPassword) {
        // 根据手机号查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = this.getOne(wrapper);

        if (user == null) {
            return false;
        }

        // 使用 BCrypt 加密新密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(newPassword));

        // 更新数据库
        return this.updateById(user);
    }
}