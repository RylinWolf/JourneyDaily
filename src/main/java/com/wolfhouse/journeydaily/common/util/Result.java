package com.wolfhouse.journeydaily.common.util;

import com.wolfhouse.journeydaily.common.enums.ResultStatusEnum;
import io.netty.util.internal.StringUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 结果封装类
 *
 * @author linexsong
 */
@Data
public class Result<T> implements Serializable {
    private T data;
    private String msg;
    private ResultStatusEnum status;


    public static <T> Result<T> success(T data, String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.data = data;
        result.status = ResultStatusEnum.SUCCESS;
        return result;
    }

    public static <T> Result<T> success(T data) {
        return Result.success(data, StringUtil.EMPTY_STRING);
    }

    public static Result<String> success() {
        return Result.success(null);
    }

    public static <T> Result<T> error(T data, String msg) {
        Result<T> res = new Result<>();
        res.setStatus(ResultStatusEnum.ERROR);
        res.setData(data);
        res.setMsg(msg);
        return res;
    }

    public static <T> Result<T> error() {
        return Result.error(null, "");
    }

    public static <T> Result<T> failed(T data, String msg) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setMsg(msg);
        result.setStatus(ResultStatusEnum.FAILED);
        return result;
    }

    public static <T> Result<T> failed(T data) {
        return Result.failed(data, "");
    }


    public static Result<String> failed() {
        return Result.failed(null);
    }

    public static <T> Result<T> failedIfBlank(T data) {
        return Result.failedIfBlank(data, "");
    }

    public static <T> Result<T> failedIfBlank(T data, String errorMsg) {
        return BeanUtil.isBlank(data) ? Result.failed(data, errorMsg) : Result.success(data);
    }

}
