package com.ziroom.ferrari.enums;

import lombok.Getter;

/**
 * Created by cheshun on 2017/3/20.
 */
@Getter
public enum MsgStatusEnum {

    MSG_UN_SEND(0, "未发送"),
    MSG_SEND_FAILURE(1,"发送失败"),
    MSG_SEND_SUCCESS(2,"发送成功"),
    MSG_RECEIVE_FAILURE(3,"消费失败"),
    MSG_RECEIVE_SUCCESS(4,"消费成功")
    ;

    private Integer code;
    private String desc;

    MsgStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}