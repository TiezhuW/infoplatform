package com.john.controller;

import com.john.entity.Result;
import com.john.entity.User;
import com.john.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Validated @RequestBody User user) {
        int ret;
        try {
            ret = userService.save(user);
        } catch (Exception e) {
            ret = 0;
        }
        if (ret == 1) {
            return Result.success("注册成功");
        } else {
            return Result.fail("注册失败");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/update")
    public Result update(@Validated @RequestBody User user) {
        String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUsername.equals(user.getUsername())) {
            int ret;
            try {
                ret = userService.update(user);
            } catch (Exception e) {
                ret = 0;
            }
            if (ret == 1) {
                return Result.success("用户信息修改成功");
            } else {
                return Result.fail("用户信息修改失败");
            }
        } else {
            return Result.fail("用户信息修改失败");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/current")
    public User getCurrentUser() {
        User user = new User();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = userService.findByUsername(username).getId();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/findByUsername/{username}")
    public Result findByUsername(@PathVariable("username") String username) {
        String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUsername.equals(username)) {
            User user = userService.findByUsername(username);
            return Result.success("获取用户信息成功", user);
        } else {
            return Result.fail("获取用户信息失败");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/isAdmin")
    public boolean isAdmin() {
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.get(0).getAuthority().equals("ROLE_ADMIN")) {
            return true;
        } else {
            return false;
        }
    }
}
