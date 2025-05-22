package com.wolfhouse.journeydaily.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author linexsong
 */
public enum AdminPurviewEnum {
    /**
     * ALL - 全部权限
     * USER_ONLY - 仅用户
     * JOURNEY_ONLY - 仅日记
     * SUPPER - 仅管理员
     */
    ALL(0),
    USER_ONLY(1),
    JOURNEY_ONLY(2),
    SUPPER(4);


    /**
     * 权限
     */
    @EnumValue
    @JsonValue
    public final Integer purview;

    AdminPurviewEnum(Integer purview) {
        this.purview = purview;
    }
}
