package com.mengnankk.takeout.utils;

public class ThreadLocalUtil {

    private static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * Set data to ThreadLocal
     * @param value data to be set
     * @param <T> type of data
     */
    public static <T> void set(T value) {
        THREAD_LOCAL.set(value);
    }

    /**
     * Get data from ThreadLocal
     * @return data from ThreadLocal
     * @param <T> type of data
     */
    @SuppressWarnings("unchecked")
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    /**
     * Remove data from ThreadLocal
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}