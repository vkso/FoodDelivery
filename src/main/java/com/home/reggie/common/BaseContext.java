package com.home.reggie.common;

/**
 * 基于 ThreadLocal 封装的工具类，用于保存和读取当前用户的 id
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
