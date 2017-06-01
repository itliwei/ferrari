package com.ziroom.ferrari.produce;

import com.alibaba.druid.support.json.JSONUtils;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.ChangeTypeEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.rent.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by homelink on 2017/5/31 0031.
 */
@Slf4j
@Service
public class MqProduce {

    @Autowired
    private static RabbitMqSendClient rabbitMqSendClient;
    @Autowired
    private RabbitConnectionFactory rabbitConnectionFactory;

    public void sendToMq(QueueName queueName ,MessageData messageData) {
        Transaction t = Cat.newTransaction("Service", "Sending data to mq");
        log.info("Trace data circle life start");
        log.info("Sending data to RabbitMq to Channel[{}]", queueName.getSystem() + "-"
                + queueName.getModule() + "-" + queueName.getFunction());
        try {
            Cat.logEvent("INFO", "Sending data to MQ");
            String s = messageData.toString();
            rabbitMqSendClient.sendQueue(queueName, messageData.toString());
            Cat.logEvent("INFO", Thread.currentThread().getStackTrace()[1].getMethodName(), Transaction.SUCCESS, JSONUtils.toJSONString(messageData));
            Cat.logMetricForCount("Sending to MQ");
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception ex) {
            log.error("Error occurred during sending data to RabbitMq", ex);
            Cat.logError(ex);
            t.setStatus(ex);
            throw new GaeaRabbitMQException("Error in sending message to mq", ex);
        } finally {
            t.complete();
        }
    }

    public  static void  main(String[] args){

        RabbitConnectionFactory rabbitConnectionFactory = new RabbitConnectionFactory();
        MqProduce mqProduce = new MqProduce();
        MessageData messageData = new MessageData();
        messageData.setMsgId("aaa");
        messageData.setChangeTime(DateUtils.format2Long(new Date()));
        messageData.setProduceTime(DateUtils.format2Long(new Date()));
        messageData.setChangeKey(ChangeTypeEnum.ADD.getCode());
        messageData.setChangeData(null);
        rabbitMqSendClient = new RabbitMqSendClient(rabbitConnectionFactory);
        QueueNameEnum ams = QueueNameEnum.AMS;
        QueueName queueName = new QueueName(ams.getSystem(),ams.getModule(),ams.getFunction());
        mqProduce.sendToMq(queueName,messageData);
    }
}
