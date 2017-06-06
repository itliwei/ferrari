package com.ziroom.ferrari.task;

import com.ziroom.ferrari.dao.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.service.impl.MqProduceServiceImpl;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.rent.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 发送消息线程任务
 * Created by homelink on 2017/6/2 0002.
 */
@Slf4j
public class SendToMqTask implements Runnable {
    @Autowired
    private MqProduceServiceImpl mqProduceServiceImpl;
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;

    private QueueNameEnum queueNameEnum;
    private MessageData messageData;
    private DataChangeMessage dataChangeMessage;

    public SendToMqTask(QueueNameEnum queueNameEnum , MessageData messageData,
                        DataChangeMessage dataChangeMessage) {
        this.messageData = messageData;
        this.dataChangeMessage = dataChangeMessage;
        this.queueNameEnum = queueNameEnum;
    }

    @Override
    public void run() {
        log.info("SendToMqTask :{} ,ThreadId:{} run：{}",queueNameEnum.getFunction(),Thread.currentThread().getName(),queueNameEnum.getModule(),messageData.toJsonStr());
        try {
            //发送时间
            messageData.setProduceTime(DateUtils.format2Long(new Date()));
            mqProduceServiceImpl.sendToMq(queueNameEnum,messageData);
            //发送成功，更新日志记录
            dataChangeMessage.setMsgStatus(MsgStatusEnum.MSG_SEND_SUCCESS.getCode());
            dataChangeMessageDao.update(dataChangeMessage);
        }catch (GaeaRabbitMQException exp){
            log.error("SendToMqTask sendToMq exception :{}" ,exp);
            //TODO 异常处理，更改数据状态。要有主键ID！！！消息重试是直接重试还是放入一个 重试队列中？
            dataChangeMessage.setMsgStatus(MsgStatusEnum.MSG_SEND_FAILURE.getCode());
            dataChangeMessageDao.update(dataChangeMessage);
        }
    }
}
