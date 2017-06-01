package com.ziroom.ferrari;

import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.ChangeTypeEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.produce.MqProduceClient;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.rent.common.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * RabbitMq注入测试类
 *
 * @author dongl50@ziroom.com
 * @version 1.0
 * @date 25/2/2017 2:54 PM
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class RabbitMqConfigTest {

    @Test
    public void rabbitMqSendClientTest() throws Exception {
        MessageData messageData = new MessageData();
        messageData.setMsgId("aaa");
        messageData.setChangeTime(DateUtils.format2Long(new Date()));
        messageData.setProduceTime(DateUtils.format2Long(new Date()));
        messageData.setChangeKey(ChangeTypeEnum.ADD.getCode());
        messageData.setChangeData(null);

        QueueName queueName = new QueueName(QueueNameEnum.AMS.getSystem(),QueueNameEnum.AMS.getModule(),QueueNameEnum.AMS.getFunction());
        MqProduceClient mqProduceClient = new MqProduceClient();

        mqProduceClient.sendToMq(queueName,messageData);

    }

    @Test
    public void rabbitMqQueueReceiverTest() throws Exception {


    }
}
