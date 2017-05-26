package com.ziroom.ferrari.constants;

/**
 * @author dongh38@ziroom.com
 * @version 1.0.0
 */
public enum  ErrorCode {


    ARGUMENT_ERROR("400","参数错误"),

    INTER_ERROR("500","服务器内部错误");

    private String errorCode;

    private String errorMessage;

    ErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
