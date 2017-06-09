package com.ziroom.ferrari.domain;

import com.alibaba.fastjson.JSON;
import com.ziroom.rent.common.orm.dao.annotation.Column;
import com.ziroom.rent.common.orm.dao.annotation.Table;
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
@Table(value = "t_data_change_message", sequence = "seq_t_data_change_message")
public class DataChangeMessageEntity extends IdEntity {
    //消息ID
    @Column(value = "msg_id")
    private String msgId;
    //系统
    @Column(value = "msg_system")
    private String msgSystem;
    //模块
    @Column(value = "msg_module")
    private String msgModule;
    //功能
    @Column(value = "msg_function")
    private String msgFunction;
    //消息状态
    @Column(value = "msg_status")
    private int msgStatus;
    //变更数据实体
    @Column(value = "change_entity_name")
    private String changeEntityName;
    //数据的主键
    @Column(value = "change_key")
    private String changeKey;
    //操作类型 0新增 1修改 2删除 changeType enum
    @Column(value = "change_type")
    private int changeType;
    //数据变更时间
    @Column(value = "change_time")
    private long changeTime;
    //消息更改时间
    @Column(value = "produce_time")
    private long produceTime;
    //变更内容
    @Column(value = "change_data")
    private String changeData;
    //消息消费时间
    @Column(value = "consume_time")
    private long consumeTime;

    public String toJsonStr() {
        return JSON.toJSONString(this);
    }

}
