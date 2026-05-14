package com.example.tutorplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tutorplatform.entity.User;
import com.example.tutorplatform.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务类
 * 继承 ServiceImpl 可以直接使用 Mybatis-Plus 提供的 count, save, getOne 等方法
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 注册功能
     */
    public boolean register(String phone, String password, String role, String nickname) {
        // 1. 检查手机号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        if (this.count(wrapper) > 0) {
            return false;
        }

        // 2. 创建新用户并使用 BCrypt 加密密码
        User user = new User();
        user.setPhone(phone);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        user.setNickname(nickname);

        return this.save(user);
    }

    /**
     * 登录验证
     */
    public User login(String phone, String password) {
        // 1. 根据手机号查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = this.getOne(wrapper);

        if (user == null) {
            return null;
        }

        // 2. 验证密码是否匹配
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, user.getPassword())) {
            return user; // 登录成功
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

    /**
     * 统计老师总数 (首页统计功能)
     */
    public long countTeachers() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 筛选角色为 'teacher' 的用户
        wrapper.eq(User::getRole, "teacher");
        // 直接使用 ServiceImpl 提供的 count 方法
        return this.count(wrapper);
    }

}