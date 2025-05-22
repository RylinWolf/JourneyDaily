package com.wolfhouse.journeydaily.common.aspect;

import com.wolfhouse.journeydaily.common.annotation.NotBlankArgs;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * @author linexsong
 */
@Aspect
@Component
@Slf4j
public class MethodVerifyAspect {
    @Pointcut("execution(* com.wolfhouse.journeydaily.service..*(..)) && @annotation(com.wolfhouse.journeydaily.common.annotation.NotBlankArgs)")
    public void argsVerifyPointcut() {
    }

    @Before("argsVerifyPointcut()")
    public void argsVerify(JoinPoint joinPoint) {
        log.info("切入: {}", joinPoint.getSignature().getName());
        // 获取目标方法实际参数
        Object[] args = joinPoint.getArgs();
        // 获取方法
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Method method = sig.getMethod();

        // 获取方法注解
        NotBlankArgs anno = method.getAnnotation(NotBlankArgs.class);
        String[] requiredNotBlankFields = anno.value();

        // 方法参数名称
        String[] argNames = sig.getParameterNames();

        // 根据方法注解获取需要标记不为空的字段名，通过反射获取方法字段，进行比较
        if (BeanUtil.isBlank(requiredNotBlankFields)) {
            // 未指定需要验证的参数
            checkArgs(argNames, args);
            return;
        }

        // 验证指定的参数
        checkArgs(argNames, args, requiredNotBlankFields);

    }

    /**
     * 验证参数是否为空
     *
     * @param argNames               方法参数名称列表
     * @param args                   方法参数列表
     * @param requiredNotBlankFields 指定需要验证的参数列表
     */
    private void checkArgs(String[] argNames, Object[] args, String[] requiredNotBlankFields) {
        // argName: 参数名称
        for (int i = 0; i < argNames.length; i++) {
            // field: 标记为不为空的参数名
            for (String field : requiredNotBlankFields) {
                if (!argNames[i].equals(field)) {
                    continue;
                }
                if (BeanUtil.isBlank(args[i])) {
                    // 若为空，则抛出异常
                    throw ServiceException.fieldRequired(field);
                }
                break;
            }
        }
    }

    /**
     * 验证参数是否为空
     *
     * @param argNames 方法参数名称列表
     * @param args     方法参数列表
     */
    private void checkArgs(String[] argNames, Object[] args) {
        for (int i = 0; i < argNames.length; i++) {
            if (BeanUtil.isBlank(args[i])) {
                throw ServiceException.fieldRequired(argNames[i]);
            }
        }
    }
}
