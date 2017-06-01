package com.ziroom.ferrari;

import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.ChangeTypeEnum;
import com.ziroom.ferrari.produce.MqProduce;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.gaea.mq.rabbitmq.receive.RabbitMqMessageListener;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.RabbitMqQueueReceiver;
import com.ziroom.rent.common.util.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RabbitMq注入测试类
 *
 * @author dongl50@ziroom.com
 * @version 1.0
 * @date 25/2/2017 2:54 PM
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
public class RabbitMqConfigTest {

    @Autowired
    private RabbitConnectionFactory rabbitConnectionFactory;

    @Autowired
    private RabbitMqSendClient rabbitMqSendClient;
    @Autowired
    private MqProduce mqProduce;

    @Autowired
    private RabbitMqQueueReceiver rabbitMqQueueReceiver;

    private QueueName queueName = new QueueName("lukas", "ams", "index");


    @Before
    public void rabbitConnectionaFactoryTest() throws Exception {
        rabbitConnectionFactory.init();
        System.out.println(rabbitConnectionFactory.getEnvironment());
        System.out.println("Hello lukas");
    }

    @Test
    public void rabbitMqSendClientTest() throws Exception {
        MessageData messageData = new MessageData();
        messageData.setMsgId("aaa");
        messageData.setChangeTime(DateUtils.format2Long(new Date()));
        messageData.setProduceTime(DateUtils.format2Long(new Date()));
        messageData.setChangeKey(ChangeTypeEnum.ADD.getCode());
        messageData.setChangeData(null);

        mqProduce.sendToMq(messageData);
        rabbitMqSendClient.sendQueue(queueName, "2");
    }

    @Test
    public void rabbitMqQueueReceiverTest() throws Exception {


    }
}
