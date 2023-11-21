package com.wei.reggie_tack_out.common;

/* 基于ThreadLocal的封装工具类，用于保护和获取当前登录用户id */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
