package com.ziroom.ferrari.vo;

import com.ziroom.ferrari.enums.ChangeTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据变更消息
 * 实际往MQ发送的消息
 * @Author zhoutao
 * @Date 2017/6/7
 */
@Setter
@Getter
@ToString
public class DataChangeMessage {
    //变更数据实体名称
    private String changeEntityName;
    //业务上唯一区分一条数据的主键，如invRoomCode
    private String changeKey;
    //操作类型 0新增 1修改 2删除 changeType enum
    private ChangeTypeEnum changeType;
    //数据变更时间 yyyyMMddHHmmssSSS
    private long changeTime;
    //变更内容 TODO:补充详细说明
    private String changeData;
}
