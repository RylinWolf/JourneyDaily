package com.wolfhouse.journeydaily.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.wolfhouse.journeydaily.common.constant.ResultConstant;
import lombok.Getter;

/**
 * @author linexsong
 */
@Getter
public enum ResultStatusEnum {
    /**
     * 表示操作成功的状态。
     * 该枚举常量的状态码为 0，对应的消息为 "success"。
     */
    SUCCESS(0, ResultConstant.SUCCESS), FAILED(1, ResultConstant.FAILED), ERROR(2, ResultConstant.ERROR);
    private final Integer statusCode;
    private final String msg;

    ResultStatusEnum(Integer statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    @JsonValue
    public Integer getCode() {
        return this.statusCode;
    }
}
