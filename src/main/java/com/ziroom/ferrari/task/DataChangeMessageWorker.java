package com.ziroom.ferrari.task;

import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.exception.DataChangeMessageSendException;
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
        StringBuilder sb = new StringBuilder();
        long start = System.currentTimeMillis();
        sb.append("DataChangeMessageWorker.run ：");
        sb.append("|发送数据：");
        sb.append(dataChangeMessageEntity.toString());
        sb.append("|发送状态：");
        boolean sendMsgToMqSuccess = true;
        try {
            QueueName queueName = new QueueName(dataChangeMessageEntity.getMsgSystem(), dataChangeMessageEntity.getMsgModule(),
                    dataChangeMessageEntity.getMsgFunction());
            DataChangeMessage dataChangeMessage = MessageConvert.convertDataChangeMessageEntity(dataChangeMessageEntity);
            rabbitMqSendClient.sendQueue(queueName, dataChangeMessage.toJsonStr());
            sb.append("|success");
        } catch (Exception exp) {
            sendMsgToMqSuccess = false;
            sb.append("|error:");
            sb.append(exp);
            log.error("SendToMqTask sendToMq GaeaRabbitMQException :{}", exp);

        }
        sb.append("|更改消息状态：");
        //消息发送mq完毕更新发送状态
        Update update = new Update();
        if (sendMsgToMqSuccess) {
            update.set("msgStatus", MsgStatusEnum.MSG_SEND_SUCCESS.getCode());
        } else {
            update.set("msgStatus", MsgStatusEnum.MSG_SEND_FAILURE.getCode());
        }
        try {
            dataChangeMessageDao.updateById(dataChangeMessageEntity.getId(), update);
            sb.append("success");
        }catch (RuntimeException e){
            sb.append("failure:"+e.getMessage());
            throw new DataChangeMessageSendException("DataChangeMessageProducer.sendMsg Error", e);
        }finally {
            sb.append("|Time:" + (System.currentTimeMillis() - start));
            log.info(sb.toString());
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
