package com.wolfhouse.journeydaily.common.exceptions;

import lombok.NoArgsConstructor;

/**
 * @author linexsong
 */
@NoArgsConstructor
public class JdBaseException extends RuntimeException {
    public JdBaseException(String message) {
        super(message);
    }
}
