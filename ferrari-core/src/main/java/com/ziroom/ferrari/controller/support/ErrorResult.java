package com.ziroom.ferrari.controller.support;

/**
 * @author dongh38@ziroom
 */
public class ErrorResult {

    private String code;

    private String message;

    public ErrorResult() {
    }

    public ErrorResult(String message) {
        this.message = message;
    }

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
