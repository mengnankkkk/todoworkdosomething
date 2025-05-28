package com.mengnankk.shopping.utils;

import com.mengnankk.shopping.model.User;

public class UserHolder {
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    /**
     * 保存用户信息到ThreadLocal
     */
    public static void set(User user) {
        tl.set(user);
    }

    /**
     * 获取ThreadLocal中的用户信息
     */
    public static User get() {
        return tl.get();
    }

    /**
     * 获取ThreadLocal中的用户ID
     */
    public static Long getUserId() {
        User user = get();
        return user != null ? user.getId() : null;
    }

    /**
     * 清除ThreadLocal中的用户信息
     */
    public static void remove() {
        tl.remove();
    }
} 