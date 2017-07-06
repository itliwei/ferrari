package com.ziroom.ferrari.vo;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.ferrari.enums.ChangeTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;


/**
 * 数据变更消息
 * 实际往MQ发送的消息
 *
 * @Author zhoutao
 * @Date 2017/6/7
 */
@Setter
@Getter
@ToString
public class DataChangeMessage {
    //消息ID,jar包内生产
    private String msgId;
    //变更数据实体名称
    private String changeEntityName;
    //业务上唯一区分一条数据的主键，如invRoomCode
    private String changeKey;
    //操作类型 0新增 1修改 2删除 changeType enum
    private ChangeTypeEnum changeType;
    //数据变更时间 yyyyMMddHHmmssSSS
    private long changeTime;
    //消息生产时间 yyyyMMddHHmmssSSS
    private long produceTime;
    //变更内容
    private String changeData;

    public String toJsonStr() {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(this);
        String changeType = jsonObject.getString("changeType");
        //修改类型转换为int
        if (StringUtils.isNotBlank(changeType)) {
            ChangeTypeEnum typeEnum = ChangeTypeEnum.valueOf(changeType);
            jsonObject.put("changeType", typeEnum.getCode());
        }
        return jsonObject.toString();
    }
}
