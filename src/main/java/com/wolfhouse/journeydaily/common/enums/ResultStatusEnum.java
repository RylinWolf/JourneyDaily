package com.wolfhouse.journeydaily.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.wolfhouse.journeydaily.common.constant.ResultConstant;
import lombok.Getter;

/**
 * @author linexsong
 */
@Getter
public enum ResultStatusEnum {

    SUCCESS(0, ResultConstant.SUCCESS),
    FAILED(1, ResultConstant.FAILED),
    ERROR(2, ResultConstant.ERROR);
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
