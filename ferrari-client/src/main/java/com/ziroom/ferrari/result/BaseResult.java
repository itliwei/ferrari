package com.ziroom.ferrari.result;

/**
 * Created by homelink on 2017/6/5 0005.
 */
public class BaseResult {
    //返回code
    private int code;
    //返回message
    private String message;

    public BaseResult() {
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
