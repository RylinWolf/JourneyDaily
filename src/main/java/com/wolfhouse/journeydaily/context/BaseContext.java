package com.wolfhouse.journeydaily.context;

import java.util.Objects;

/**
 * @author linexsong
 */
public class BaseContext {
    private static final ThreadLocal<Long> LOGIN_USER = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        LOGIN_USER.set(userId);
    }

    public static void removeUserId() {
        LOGIN_USER.remove();
    }

    public static Long getUserId() {
        return LOGIN_USER.get();
    }

    public static Boolean isCurrent(Long userId) {
        return userId != null && getUserId() != null && Objects.equals(userId, BaseContext.getUserId());
    }


}
