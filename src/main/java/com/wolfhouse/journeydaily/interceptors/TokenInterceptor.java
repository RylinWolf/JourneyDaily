package com.wolfhouse.journeydaily.interceptors;

import com.wolfhouse.journeydaily.common.constant.JwtConstant;
import com.wolfhouse.journeydaily.common.properties.JwtProperties;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token 拦截器，解析并保存 token 信息
 *
 * @author linexsong
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    private final JwtProperties properties;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 获取 token 并解析
        String token = request.getHeader(JwtConstant.TOKEN_NAME);
        if (!BeanUtil.isBlank(token)) {
            try {
                JwtInterceptor.extractToken(response, properties, token);
            } catch (Exception ignored) {
            }
        }
        return true;
    }
}
