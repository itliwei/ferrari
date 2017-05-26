package com.ziroom.ferrari.exception;

import com.ziroom.ferrari.constants.ErrorCode;

/**
 * @author dongh38@ziroom.com
 * @version 1.0.0
 */
public class FerrariException extends RuntimeException {

    public ErrorCode errorCode;

    private String message;

    public FerrariException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
