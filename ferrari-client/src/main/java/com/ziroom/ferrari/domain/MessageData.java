package com.ziroom.ferrari.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by homelink on 2017/5/31 0031.
 */
@Setter
@Getter
@ToString
public class MessageData {
    //数据库主键ID
    private Long id;
    //消息ID
    private String msgId;
    //变更数据实体名称
    private String changeEntityName;
    //数据的主键
    private String changeKey;
    //操作类型 0新增 1修改 2删除 changeType enum
    private Integer changeType;
    //数据变更时间
    private Long changeTime;
    //消息更改时间
    private Long produceTime;
    //变更内容
    private String changeData;

    public String toJsonStr() {
        return JSON.toJSONString(this);
    }

}
