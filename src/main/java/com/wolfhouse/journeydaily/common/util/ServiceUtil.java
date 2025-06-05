package com.wolfhouse.journeydaily.common.util;

import com.wolfhouse.journeydaily.common.constant.UserConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.properties.JwtProperties;
import com.wolfhouse.journeydaily.context.BaseContext;

import java.util.HashMap;

/**
 * @author linexsong
 */
public class ServiceUtil {

    /**
     * 获取用户 ID，若未登录则抛出异常
     *
     * @return userId
     */
    public static Long userIdOrException() {
        return userIdOrException(ServiceException.unauthorized());
    }

    /**
     * 获取用户 ID，若未登录则抛出异常
     *
     * @return userId
     */
    public static Long userIdOrException(RuntimeException e) {
        Long userId = BaseContext.getUserId();
        if (BeanUtil.isBlank(userId)) {
            throw e;
        }
        return userId;
    }

    public static String getToken(JwtProperties properties, Long userId) {
        // 设置 token
        var claim = new HashMap<String, Object>(1);
        claim.put(UserConstant.USER_ID, userId);
        return JwtUtil.createJwt(properties.getSecret(), properties.getTtl(), claim);
    }
}
