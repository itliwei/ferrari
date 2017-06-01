package com.ziroom.ferrari.enums;

import lombok.Getter;

/**
 * Created by cheshun on 2017/3/20.
 */
@Getter
public enum ChangeTypeEnum {

    ADD(0, "新增"),
    UPDATE(1, "修改"),
    DELETE(2, "删除");

    private int code;
    private String desc;

    ChangeTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
