package com.wolfhouse.journeydaily.common.exceptions;

import com.wolfhouse.journeydaily.common.constant.ServiceExceptionConstant;

/**
 * 服务异常类
 *
 * @author linexsong
 */
public class ServiceException extends JdBaseException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException() {
        super();
    }

    /**
     * 字段缺失异常
     *
     * @param field 缺失的字段名
     * @return 服务异常类实例
     */
    public static ServiceException fieldRequired(String field) {
        return new ServiceException(field + ServiceExceptionConstant.FILED_REQUIRED);
    }

    public static ServiceException fieldRequired() {
        return new ServiceException(ServiceExceptionConstant.FILED_REQUIRED);
    }

    public static ServiceException unauthorized() {
        return new ServiceException(ServiceExceptionConstant.UNAUTHORIZED);
    }

    public static ServiceException requestNotAllowed() {
        return new ServiceException(ServiceExceptionConstant.NOT_ALLOWED);
    }

    public static ServiceException authorizedFailed() {
        return new ServiceException(ServiceExceptionConstant.AUTHORIZE_FAILED);
    }

    public static ServiceException loginRequired() {
        return new ServiceException(ServiceExceptionConstant.LOGIN_REQUIRED);
    }

}
