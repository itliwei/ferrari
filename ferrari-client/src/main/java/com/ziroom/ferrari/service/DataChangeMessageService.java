package com.ziroom.ferrari.service;

import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.result.BaseResult;

/**
 * DataChangeMessage service接口
 * Created by liwei on 2017/6/5 0005.
 */
public interface DataChangeMessageService {
    /**
     * 插入数据
     * @param dataChangeMessage
     * @return
     */
    int insert(DataChangeMessage dataChangeMessage);
}
