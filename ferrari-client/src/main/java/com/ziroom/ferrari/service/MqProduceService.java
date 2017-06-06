package com.ziroom.ferrari.service;

import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.result.BaseResult;
import org.springframework.stereotype.Service;

/**
 * Created by homelink on 2017/6/5 0005.
 */
public interface MqProduceService {
    BaseResult sendToMq(QueueNameEnum queueNameEnum, MessageData messageData);
}
