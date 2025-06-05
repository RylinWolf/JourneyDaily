package com.wolfhouse.journeydaily.common.exceptions;

import com.wolfhouse.journeydaily.common.constant.BeanUtilExceptionConstant;
import lombok.NoArgsConstructor;

/**
 * Bean 工具异常类
 *
 * @author linexsong
 */
@NoArgsConstructor
public class BeanUtilException extends JdBaseException {
    public BeanUtilException(String message) {
        super(message);
    }

    public static BeanUtilException propertiesCopyFailed(Exception e) {
        return new BeanUtilException(BeanUtilExceptionConstant.COPY_PROPERTIES_ERROR + e);
    }
}
