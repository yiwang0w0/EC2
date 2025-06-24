package com.example.ecbackend.service;

import com.example.ecbackend.entity.User;

public interface UserService {
    User findByUsername(String username);
    void register(User user);
}
