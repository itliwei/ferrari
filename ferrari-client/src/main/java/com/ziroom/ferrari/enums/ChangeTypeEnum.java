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

    // getDesc
    public static String matchDesc(int code) {
        for (ChangeTypeEnum exType : ChangeTypeEnum.values()) {
            if (exType.getCode() == code) {
                return exType.desc;
            }
        }
        return null;
    }
    //通过code值获取枚举值
    public static ChangeTypeEnum getExtTypeByCode(int code) {
        for (ChangeTypeEnum exType : ChangeTypeEnum.values()) {
            if (exType.getCode() == code) {
                return exType;
            }
        }
        return null;
    }

}
