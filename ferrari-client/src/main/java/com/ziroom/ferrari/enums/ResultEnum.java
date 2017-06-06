package com.ziroom.ferrari.enums;

import lombok.Getter;

/**
 * Created by cheshun on 2017/3/20.
 */
@Getter
public enum ResultEnum {

    SUCCESS(200, "success","发送成功"),
    FAILURE(400,"failure","发送失败"),
    ;

    private Integer code;
    private String status;
    private String message;

    ResultEnum(Integer code,String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

}
