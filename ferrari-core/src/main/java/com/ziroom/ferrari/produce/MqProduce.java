package com.ziroom.ferrari.produce;

import com.alibaba.druid.support.json.JSONUtils;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.ziroom.ferrari.MessageData;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


/**
 * Created by homelink on 2017/5/31 0031.
 */
@Slf4j
public class MqProduce {
    @Value("${ams.lukas.mq.system}")
    private String mqSystem;

    @Value("${ams.lukas.mq.module}")
    private String mqModule;

    @Value("${ams.lukas.mq.function}")
    private String mqFunction;
    @Autowired
    private RabbitMqSendClient rabbitMqSendClient;
    @Autowired
    private RabbitConnectionFactory rabbitConnectionFactory;

    public void sendToMq(MessageData messageData) {
        Transaction t = Cat.newTransaction("Service", "Sending data to mq");

        log.info("Trace data circle life start");
        log.info("Sending data to RabbitMq to Channel[{}]", mqSystem + "-" + mqModule + "-" + mqFunction);
        try {
            QueueName queueName = new QueueName(mqSystem, mqModule, mqFunction);
            Cat.logEvent("INFO", "Sending data to MQ");
            rabbitMqSendClient.sendQueue(queueName, JSONUtils.toJSONString(messageData));
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
        rabbitConnectionFactory.init();
    }
}
