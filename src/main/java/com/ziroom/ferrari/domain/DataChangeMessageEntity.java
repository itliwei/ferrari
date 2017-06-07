package com.ziroom.ferrari.domain;

import com.ziroom.rent.common.orm.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author zhoutao
 * @Date 2017/6/7
 */
@Setter
@Getter
@ToString
public class DataChangeMessageEntity extends IdEntity {
    //消息ID
    private String msgId;
    //系统
    private String msgSystem;
    //模块
    private String msgModule;
    //功能
    private String msgFunction;
    //消息状态
    private int msgStatus;
    //变更数据实体
    private String changeEntityName;
    //数据的主键
    private String changeKey;
    //操作类型 0新增 1修改 2删除 changeType enum
    private int changeType;
    //数据变更时间
    private long changeTime;
    //消息更改时间
    private long produceTime;
    //变更内容
    private String changeData;
    //消息消费时间
    private long consumeTime;
}
