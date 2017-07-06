package com.ziroom.ferrari.task;

import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.exception.DataChangeMessageSendException;
import com.ziroom.ferrari.vo.DataChangeMessage;
import com.ziroom.gaea.mq.rabbitmq.PublishSubscribeType;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.ExchangeName;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.entity.RoutingKey;
import com.ziroom.rent.common.orm.query.Update;
import lombok.extern.slf4j.Slf4j;


/**
 * 执行发送消息到MQ任务的worker
 * Created by homelink on 2017/6/7 0007.
 */
@Slf4j
public class DataChangeMessageWorker implements Runnable, Comparable<DataChangeMessageWorker> {
    private DataChangeMessageDao dataChangeMessageDao;
    private RabbitMqSendClient rabbitMqSendClient;

    private DataChangeMessageEntity dataChangeMessageEntity;

    public DataChangeMessageWorker(DataChangeMessageEntity dataChangeMessageEntity,
                                   RabbitMqSendClient rabbitMqSendClient, DataChangeMessageDao dataChangeMessageDao) {
        this.dataChangeMessageEntity = dataChangeMessageEntity;
        this.rabbitMqSendClient = rabbitMqSendClient;
        this.dataChangeMessageDao = dataChangeMessageDao;
    }

    @Override
    public void run() {

        StringBuilder sb = new StringBuilder();
        long start = System.currentTimeMillis();
        sb.append("DataChangeMessageWorker.run ：");
        sb.append("|发送数据：");
        sb.append(dataChangeMessageEntity.toString());
        sb.append("|发送状态：");
        boolean sendMsgToMqSuccess = true;
        try {
            sendTopicMsg(dataChangeMessageEntity);
            sb.append("|success");
        } catch (Exception exp) {
            sendMsgToMqSuccess = false;
            sb.append("|error:");
            sb.append(exp.getMessage());
            log.error("DataChangeMessageProducer.sendMsg MQ Error", exp);

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
        } catch (RuntimeException e) {
            sb.append("failure:" + e.getMessage());
            throw new DataChangeMessageSendException("DataChangeMessageProducer.sendMsg DB Error", e);
        } finally {
            sb.append("|Time:" + (System.currentTimeMillis() - start));
            log.info(sb.toString());
        }
    }

    public void sendQueueMsg(DataChangeMessageEntity dataChangeMessageEntity) throws Exception {
        DataChangeMessage dataChangeMessage = MessageConvert.convertDataChangeMessageEntity(dataChangeMessageEntity);
        QueueName queueName = new QueueName(dataChangeMessageEntity.getMsgSystem(), dataChangeMessageEntity.getMsgModule(),
                dataChangeMessageEntity.getMsgFunction());
        rabbitMqSendClient.sendQueue(queueName, dataChangeMessage.toJsonStr());

    }

    public void sendTopicMsg(DataChangeMessageEntity dataChangeMessageEntity) throws Exception {
        DataChangeMessage dataChangeMessage = MessageConvert.convertDataChangeMessageEntity(dataChangeMessageEntity);
        ExchangeName exchangeName = new ExchangeName(dataChangeMessageEntity.getMsgSystem(), dataChangeMessageEntity.getMsgModule(),
                dataChangeMessageEntity.getMsgFunction());
        RoutingKey routingKey = new RoutingKey(dataChangeMessageEntity.getMsgSystem(), dataChangeMessageEntity.getMsgModule(),
                dataChangeMessageEntity.getMsgFunction());
        rabbitMqSendClient.sendTopic(exchangeName, routingKey, PublishSubscribeType.TOPIC, dataChangeMessage.toJsonStr());
    }

    @Override
    public int compareTo(DataChangeMessageWorker o) {
        long myTime = this.getDataChangeMessageEntity().getChangeTime();
        long ortherTime = o.getDataChangeMessageEntity().getChangeTime();
        return myTime > ortherTime ? 1 : (myTime == ortherTime ? 0 : -1);
    }

    public DataChangeMessageEntity getDataChangeMessageEntity() {
        return dataChangeMessageEntity;
    }


}
