package com.example.ecbackend.controller;

import com.example.ecbackend.common.Response;
import com.example.ecbackend.entity.User;
import com.example.ecbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ✅ 注册接口
    @PostMapping("/register")
    public Response<String> register(@RequestBody User user) {
        User exist = userService.findByUsername(user.getUsername());

        if (exist != null) {
            return Response.fail(1, "用户名已存在");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userService.register(user);
            return Response.success("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(2, "注册失败，服务器错误");
        }
    }

    @PostMapping("/login")
    public Response<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        User dbUser = userService.findByUsername(user.getUsername());

        // 🔍 添加调试打印
        if (dbUser != null) {
            System.out.println("输入用户名: " + user.getUsername());
            System.out.println("输入密码: " + user.getPassword());
            System.out.println("数据库密码: " + dbUser.getPassword());
            System.out.println("密码匹配结果: " + passwordEncoder.matches(user.getPassword(), dbUser.getPassword()));
        } else {
            System.out.println("用户名未找到: " + user.getUsername());
        }

        if (dbUser != null && passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            result.put("username", dbUser.getUsername());
            result.put("token", "mock-token");
            return Response.success(result);
        } else {
            return Response.fail(1, "用户名或密码错误");
        }
    }
}