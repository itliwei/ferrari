package com.ziroom.ferrari.task;

import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.executor.DataChangeMessageSendExecutor;
import com.ziroom.ferrari.vo.DataChangeMessage;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.rent.common.util.DateUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 执行任务的worker
 * Created by homelink on 2017/6/7 0007.
 */
@Slf4j
@Getter
public class DataChangeMessageWorker implements Runnable , Comparable<DataChangeMessageWorker>  {
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;
    @Autowired
    private RabbitMqSendClient rabbitMqSendClient;
    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;

    private DataChangeMessageEntity dataChangeMessageEntity;

    public DataChangeMessageWorker( DataChangeMessageEntity dataChangeMessageEntity) {
        this.dataChangeMessageEntity = dataChangeMessageEntity;
    }

    @Override
    public void run() {
        log.info("dataChangeMessage send to MQ :",dataChangeMessageEntity.toString());
        try {
            QueueName queueName = new QueueName(dataChangeMessageEntity.getMsgSystem(),dataChangeMessageEntity.getMsgModule(),
                    dataChangeMessageEntity.getMsgFunction());
            DataChangeMessage dataChangeMessage = MessageConvert.convertDataChangeMessageEntity(dataChangeMessageEntity);
            rabbitMqSendClient.sendQueue(queueName,dataChangeMessage.toJsonStr());
            dataChangeMessageDao.update(dataChangeMessageEntity);
        }catch (GaeaRabbitMQException exp){
            log.error("SendToMqTask sendToMq GaeaRabbitMQException :{}" ,exp);
            dataChangeMessageEntity.setMsgStatus(MsgStatusEnum.MSG_SEND_FAILURE.getCode());
            dataChangeMessageDao.update(dataChangeMessageEntity);
            //更改数据库状态
        } catch (Exception e) {
            log.error("SendToMqTask sendToMq exception :{}" ,e);
            dataChangeMessageEntity.setMsgStatus(MsgStatusEnum.MSG_SEND_FAILURE.getCode());
            dataChangeMessageDao.update(dataChangeMessageEntity);
        }
    }

    @Override
    public int compareTo(DataChangeMessageWorker o) {
        if (this.getDataChangeMessageEntity().getChangeTime() > o.getDataChangeMessageEntity().getChangeTime()){
            return 1;
        }else{
            return -1;
        }
    }
}
