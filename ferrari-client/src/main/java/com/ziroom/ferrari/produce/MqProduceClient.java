package com.ziroom.ferrari.produce;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.google.common.base.Strings;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.ChangeTypeEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.exception.GaeaRabbitMQException;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.rent.common.application.EnvHelper;
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
public class MqProduceClient {
    @Autowired
    private RabbitMqSendClient rabbitMqSendClient;
    @Autowired
    private RabbitConnectionFactory rabbitConnectionFactory;
    //环境变量
    private String currentEnv;


    public MqProduceClient() {
        currentEnv = EnvHelper.getEnv();
        //初始化MQ连接接
        initRabbitConnection();
    }

    public void initRabbitConnection(){
        //如果factory为空，创建factory
         if(rabbitConnectionFactory == null){
             rabbitConnectionFactory = new RabbitConnectionFactory();
         }
        //如果client为空，创建client
        if (rabbitMqSendClient == null){
            rabbitMqSendClient = new RabbitMqSendClient(rabbitConnectionFactory);
        }
    }

    public void sendToMq(QueueNameEnum queueNameEnum ,MessageData messageData) {
        Transaction t = Cat.newTransaction("Service", "Sending data to mq");
        log.info("Trace data circle life start");
        log.info("Sending data to RabbitMq to Channel[{}]", queueNameEnum.getSystem() + "-"
                + queueNameEnum.getModule() + "-" + queueNameEnum.getFunction());
        try {
            QueueName queueName = new QueueName(queueNameEnum.getSystem(),
                    queueNameEnum.getModule(),queueNameEnum.getFunction());
            Cat.logEvent("INFO", "Sending data to MQ");
            rabbitMqSendClient.sendQueue(queueName, messageData.toJsonStr());
            Cat.logEvent("INFO", Thread.currentThread().getStackTrace()[1].getMethodName(), Transaction.SUCCESS, messageData.toJsonStr());
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

    public static  void main(String[] args){
        MessageData messageData = new MessageData();
//        messageData.setMsgId();
        messageData.setChangeTime(DateUtils.format2Long(new Date()));
        messageData.setProduceTime(DateUtils.format2Long(new Date()));
        messageData.setChangeKey(ChangeTypeEnum.DELETE.getCode());
        messageData.setChangeData(null);

        QueueName queueName = new QueueName(QueueNameEnum.AMS.getSystem(),QueueNameEnum.AMS.getModule(),QueueNameEnum.AMS.getFunction());
        MqProduceClient mqProduceClient = new MqProduceClient();

        mqProduceClient.sendToMq(QueueNameEnum.AMS,messageData);
    }
}