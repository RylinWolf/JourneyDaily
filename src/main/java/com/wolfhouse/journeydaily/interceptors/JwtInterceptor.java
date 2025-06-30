package com.wolfhouse.journeydaily.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolfhouse.journeydaily.common.constant.JwtConstant;
import com.wolfhouse.journeydaily.common.constant.UserConstant;
import com.wolfhouse.journeydaily.common.properties.JwtProperties;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import com.wolfhouse.journeydaily.common.util.JwtUtil;
import com.wolfhouse.journeydaily.common.util.Result;
import com.wolfhouse.journeydaily.common.util.ServiceUtil;
import com.wolfhouse.journeydaily.context.BaseContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author linexsong
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtProperties properties;


    @Autowired
    public JwtInterceptor(JwtProperties properties) {
        this.properties = properties;
    }

    /**
     * 解析 Token，保存登录信息，获取 Token 过期时间并刷新
     *
     * @param response   响应体
     * @param properties Jwt 配置
     * @param token      Token
     * @return 是否解析成功
     */
    public static boolean extractToken(@NotNull HttpServletResponse response, JwtProperties properties, String token) {
        Claims tokenClaim = JwtUtil.parseToken(token, properties.getSecret());
        // token 为空
        if (tokenClaim == null) {
            resultRespWithErr(response, Response.SC_NON_AUTHORITATIVE_INFORMATION, JwtConstant.NON_TOKEN);
            return false;
        }
        // 设置当前登录 UserId
        Long userId = Long.valueOf(tokenClaim.get(UserConstant.USER_ID).toString());
        BaseContext.setUserId(userId);
        // 获取过期时间
        Date date = new Date(Long.parseLong(tokenClaim.get(JwtConstant.EXPIRED_FIELD).toString()) * 1000);
        log.info("token 过期时间: {}", date);
        Date newDate = new Date(System.currentTimeMillis() + properties.getRefreshTtl());
        if (date.before(newDate)) {
            log.info("刷新 token: {}", newDate);
            // 在指定刷新时间后会过期
            response.setHeader(JwtConstant.TOKEN_NAME, ServiceUtil.getToken(properties, userId));
        }
        log.info("JWT 权限验证: {}", BaseContext.getUserId());
        return true;
    }

    private static void resultRespWithErr(@NotNull HttpServletResponse response, int status, String msg) {
        response.setStatus(status);
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(Result.failed(null, msg)));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            // 非请求方法
            return true;
        }

        // 提取 Token
        try {
            String token = request.getHeader(JwtConstant.TOKEN_NAME);
            if (BeanUtil.isBlank(token)) {
                resultRespWithErr(response, Response.SC_UNAUTHORIZED, JwtConstant.NON_TOKEN);
                return false;
            }
            return extractToken(response, properties, token);
        } catch (ExpiredJwtException e) {
            // Token 过期
            resultRespWithErr(response, Response.SC_UNAUTHORIZED, JwtConstant.TOKEN_HAS_EXPIRED);
            return false;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            // 非法 Token
            resultRespWithErr(response, Response.SC_NON_AUTHORITATIVE_INFORMATION, JwtConstant.TOKEN_ILLEGAL);
            return false;
        } catch (Exception e) {
            // 其他异常
            log.error("JWT 异常", e);
            resultRespWithErr(response, Response.SC_PROXY_AUTHENTICATION_REQUIRED, e.getMessage());
            return false;
        }
    }
}
