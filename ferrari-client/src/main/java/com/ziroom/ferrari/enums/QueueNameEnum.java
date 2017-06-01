package com.ziroom.ferrari.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cheshun on 2017/3/20.
 */
@Getter
public enum QueueNameEnum {

    AMS("phoenix", "building","sync","资产"),
    BUILDING("phoenix","ams", "sync","楼盘"),
    INVETORY("phoenix","inventory", "sync","库存");

    private String system;
    private String module;
    private String function;
    private String desc;

    QueueNameEnum(String system, String module, String function,String desc) {
        this.system = system;
        this.module = module;
        this.function = function;
        this.desc = desc;
    }

}
