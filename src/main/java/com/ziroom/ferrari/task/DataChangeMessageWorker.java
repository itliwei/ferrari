package com.ziroom.ferrari.task;

import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.rent.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by homelink on 2017/6/7 0007.
 */
@Slf4j
public class DataChangeMessageWorker implements Runnable{
    private CountDownLatch countDownLatch;
    private String workerName;
    private DataChangeMessageEntity dataChangeMessageEntity;

    public DataChangeMessageWorker(CountDownLatch countDownLatch, String workerName, DataChangeMessageEntity dataChangeMessageEntity) {
        this.countDownLatch = countDownLatch;
        this.workerName = workerName;
        this.dataChangeMessageEntity = dataChangeMessageEntity;
    }

    @Override
    public void run() {
        log.info("SendToMqTask :{} ,ThreadId:{} run：{}",dataChangeMessageEntity.getMsgFunction(),
                workerName, dataChangeMessageEntity.getMsgModule(),dataChangeMessageEntity.toString());
        try {
            //发送时间
            dataChangeMessageEntity.setProduceTime(DateUtils.format2Long(new Date()));
           //TODO 发送消息
        }catch (GaeaRabbitMQException exp){
            log.error("SendToMqTask sendToMq exception :{}" ,exp);
            //TODO 异常处理，更改数据状态。要有主键ID！！！消息重试是直接重试还是放入一个 重试队列中？
            dataChangeMessageEntity.setMsgStatus(MsgStatusEnum.MSG_SEND_FAILURE.getCode());
            //更改数据库状态
        }
        countDownLatch.countDown();
    }
}
