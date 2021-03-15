package com.john.service;

import com.john.entity.User;

public interface UserService {
    User findByUsername(String username);
    int save(User user);
    int update(User user);
}
