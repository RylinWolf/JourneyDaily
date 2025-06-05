package com.wolfhouse.journeydaily.handler;


import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.wolfhouse.journeydaily.common.constant.ServiceExceptionConstant;
import com.wolfhouse.journeydaily.common.exceptions.ServiceException;
import com.wolfhouse.journeydaily.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author linexsong
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result<?> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(null, e.getMessage());
    }

    @ExceptionHandler
    public Result<?> handleException(ServiceException e) {
        log.error("服务异常", e);
        return Result.failed(null, e.getMessage());
    }

    @ExceptionHandler
    public Result<?> handleException(MissingServletRequestParameterException e) {
        log.error("请求缺失参数", e);
        return Result.failed(null, e.getParameterName() + ServiceExceptionConstant.FILED_REQUIRED);
    }

    @ExceptionHandler
    public Result<?> handlerException(HttpMessageNotReadableException e) {
        log.error("报文不可读", e);
        return Result.failed(null, e.getMessage());
    }

    @ExceptionHandler
    public Result<?> handleException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.failed(null, ServiceExceptionConstant.NULL);
    }

    @ExceptionHandler
    public Result<?> handleException(BadSqlGrammarException e) {
        log.error("SQL 语法参数错误", e);
        return Result.failed(null, ServiceExceptionConstant.GRAMMAR_ERROR + e.getSQLException().getLocalizedMessage());
    }

    @ExceptionHandler
    public Result<?> handlerException(MismatchedInputException e) {
        log.error("输入类型不匹配", e);
        return Result.failed(null, ServiceExceptionConstant.INPUT_MISMATCHED);
    }

    @ExceptionHandler
    public Result<?> handlerException(NumberFormatException e) {
        log.error("数字转换异常", e);
        return Result.failed(null, ServiceExceptionConstant.NOT_NUMBER_FORMAT);
    }

}
