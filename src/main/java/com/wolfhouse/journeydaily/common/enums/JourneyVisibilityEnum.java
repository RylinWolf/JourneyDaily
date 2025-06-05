package com.wolfhouse.journeydaily.common.enums;

/**
 * @author linexsong
 */
public enum JourneyVisibilityEnum {
    /**
     * VISIBLE(1)：公开日记,
     * INVISIBLE(0): 非公开日记
     */
    VISIBLE(1), INVISIBLE(0);

    final Integer visibility;

    JourneyVisibilityEnum(Integer visibility) {
        this.visibility = visibility;
    }
}
