package com.john.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private boolean success;
    private String msg;
    private Object obj;

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static Result success(String msg) {
        return new Result(true, msg);
    }

    public static Result success(String msg, Object obj) {
        return new Result(true, msg, obj);
    }

    public static Result fail(String msg) {
        return new Result(false, msg);
    }

    public static Result fail(String msg, Object obj) {
        return new Result(false, msg, obj);
    }
}
