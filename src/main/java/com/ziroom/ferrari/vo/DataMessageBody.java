package com.ziroom.ferrari.vo;

/**
 * Created by homelink on 2017/6/8 0008.
 */
public class DataMessageBody implements Comparable<DataMessageBody>{
    private long id;
    //消息ID
    private String msgId;
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

    @Override
    public int compareTo(DataMessageBody o) {
        return this.changeTime > o.changeTime?-1:1;
    }
}
