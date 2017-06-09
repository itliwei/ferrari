package com.ziroom.ferrari.task;

import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.vo.DataChangeMessage;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.rent.common.orm.query.Update;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 执行发送消息到MQ任务的worker
 * Created by homelink on 2017/6/7 0007.
 */
@Slf4j
@Getter
public class DataChangeMessageWorker implements Runnable, Comparable<DataChangeMessageWorker> {
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;
    @Autowired
    private RabbitMqSendClient rabbitMqSendClient;
    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;

    private String jobName;
    private DataChangeMessageEntity dataChangeMessageEntity;

    public DataChangeMessageWorker(String jobName, DataChangeMessageEntity dataChangeMessageEntity) {
        this.jobName = jobName;
        this.dataChangeMessageEntity = dataChangeMessageEntity;
    }

    @Override
    public void run() {
        log.info("jobName:{},dataChangeMessage send to MQ :{}", dataChangeMessageEntity.toString());
        boolean sendMsgToMqSuccess = true;
        try {
            QueueName queueName = new QueueName(dataChangeMessageEntity.getMsgSystem(), dataChangeMessageEntity.getMsgModule(),
                    dataChangeMessageEntity.getMsgFunction());
            DataChangeMessage dataChangeMessage = MessageConvert.convertDataChangeMessageEntity(dataChangeMessageEntity);
            rabbitMqSendClient.sendQueue(queueName, dataChangeMessage.toJsonStr());
            dataChangeMessageDao.update(dataChangeMessageEntity);
        } catch (Exception exp) {
            sendMsgToMqSuccess = false;
            log.error("SendToMqTask sendToMq GaeaRabbitMQException :{}", exp);
        }

        //消息发送mq完毕更新发送状态
        Update update = new Update();
        if (sendMsgToMqSuccess) {
            update.set("msgStatus", MsgStatusEnum.MSG_SEND_SUCCESS.getCode());
        } else {
            update.set("msgStatus", MsgStatusEnum.MSG_SEND_FAILURE.getCode());
        }
        try {
            dataChangeMessageDao.updateById(dataChangeMessageEntity.getId(), update);
        }catch (RuntimeException e){
            log.error("SendToMqTask sendToMq GaeaRabbitMQException :{}", e);
        }
    }

    @Override
    public int compareTo(DataChangeMessageWorker o) {
        if (this.getDataChangeMessageEntity().getChangeTime() > o.getDataChangeMessageEntity().getChangeTime()) {
            return 1;
        }
        return -1;
    }
}
