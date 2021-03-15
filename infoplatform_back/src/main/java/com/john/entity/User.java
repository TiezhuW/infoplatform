package com.john.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class User {
    private Integer id;

    @Size(min = 3, max = 14, message = "用户名长度必须在3到14之间")
    private String username;

    @Size(min = 6, max = 14, message = "密码长度必须在6到14之间")
    private String password;

    @Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String role;
}
