package com.john.repository;

import com.john.entity.User;
import org.springframework.stereotype.Repository;

public interface UserRepository {
    User findByUsername(String username);
    int save(User user);
    int update(User user);
}
