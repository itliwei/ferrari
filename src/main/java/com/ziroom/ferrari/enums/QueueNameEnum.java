package com.ziroom.ferrari.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cheshun on 2017/3/20.
 */
@Getter
public enum QueueNameEnum {

    AMS("phoenix", "ams","sync","资产"),
    BUILDING("phoenix","building", "sync","楼盘"),
    INVENTORY("phoenix","inventory", "sync","库存");

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

    /**
     * 通过module查找queueName
     * @param module
     * @return
     */
    public static QueueNameEnum getQueueNameEnum(String system,String module,String function){
        QueueNameEnum[] values = QueueNameEnum.values();
        for (QueueNameEnum queueNameEnum :values ){
            if (queueNameEnum.getSystem().equals(system) &&
                    queueNameEnum.getModule().equals(module) &&
                    queueNameEnum.getFunction().equals(function)){
                return queueNameEnum;
            }
        }
        return null;
    }

}