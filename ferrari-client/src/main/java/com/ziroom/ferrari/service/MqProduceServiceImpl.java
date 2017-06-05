package com.ziroom.ferrari.service;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.result.BaseResult;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.rent.common.application.EnvHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by homelink on 2017/6/2 0002.
 */
@Slf4j
@Service
public class MqProduceServiceImpl implements MqProduceService{
    @Autowired
    private RabbitMqSendClient rabbitMqSendClient;
    @Autowired
    private RabbitConnectionFactory rabbitConnectionFactory;
    //环境变量
    private String currentEnv;
    public MqProduceServiceImpl() {
        //初始化MQ连接接
        initRabbitConnection();
        currentEnv = EnvHelper.getEnv();
    }

    private void initRabbitConnection(){
        //如果factory为空，创建factory
        if(rabbitConnectionFactory == null){
            rabbitConnectionFactory = new RabbitConnectionFactory();
        }
        //如果client为空，创建client
        if (rabbitMqSendClient == null){
            rabbitMqSendClient = new RabbitMqSendClient(rabbitConnectionFactory);
        }
    }

    /**
     * 发送到MQ
     * @param queueNameEnum queueNameEnum
     * @param messageData MessageData
     */
    public BaseResult sendToMq(QueueNameEnum queueNameEnum, MessageData messageData) {
        log.info("Trace data circle life start");
        log.info("Sending data to RabbitMq to Channel[{}]", queueNameEnum.getSystem() + "-"
                + queueNameEnum.getModule() + "-" + queueNameEnum.getFunction());
        try {
            QueueName queueName = new QueueName(queueNameEnum.getSystem(),
                    queueNameEnum.getModule(),queueNameEnum.getFunction());
            log.info("INFO", "Sending data to MQ");

            rabbitMqSendClient.sendQueue(queueName, messageData.toJsonStr());
            log.info("INFO", Thread.currentThread().getStackTrace()[1].getMethodName(), Transaction.SUCCESS, messageData.toJsonStr());
        } catch (Exception ex) {
            log.error("Error occurred during sending data to RabbitMq", ex);
            Cat.logError(ex);
            throw new GaeaRabbitMQException("Error in sending message to mq", ex);
        }
        return null;
    }
}
