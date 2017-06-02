package com.ziroom.ferrari.domain;

import com.ziroom.rent.common.orm.dao.annotation.Column;
import com.ziroom.rent.common.orm.dao.annotation.Table;
import lombok.*;

/**
 * @author dongh38@ziroom.com
 * @version 1.0.0
 */
@Setter
@Getter
@Table(value = "t_data_change_message")
public class DataChangeMessage {
    //主键ID
    private Long id;
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
    private Integer msgStatus;
    //变更数据实体
    @Column(value = "change_entity")
    private String changeEntity;
    //数据的主键
    @Column(value = "change_key")
    private Integer changeKey;
    //操作类型 0新增 1修改 2删除 changeType enum
    @Column(value = "change_type")
    private Integer changeType;
    //数据变更时间
    @Column(value = "change_time")
    private Long changeTime;
    //消息更改时间
    @Column(value = "produce_time")
    private Long produceTime;
    //变更内容
    @Column(value = "change_data")
    private String changeData;
    //消息消费时间
    @Column(value = "consume_time")
    private Long consumeTime;

}
