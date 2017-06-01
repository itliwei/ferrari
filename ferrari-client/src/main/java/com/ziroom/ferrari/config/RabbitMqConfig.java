package com.ziroom.ferrari.config;

import com.ziroom.ferrari.produce.MqProduceClient;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.ExecutorRabbitMqQueueReceiver;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.RabbitMqQueueReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq 注入bean
 *
 * @author dongl50@ziroom.com
 * @date 25/2/2017 2:35 PM
 * @since 1.0
 * @version 1.0
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    @Bean
    public RabbitConnectionFactory connectionFactory() {
        return new RabbitConnectionFactory();
    }

    @Bean
    public RabbitMqSendClient rabbitMqSendClient(RabbitConnectionFactory rabbitConnectionFactory) {
        rabbitConnectionFactory.init();
        RabbitMqSendClient client = new RabbitMqSendClient(rabbitConnectionFactory);
        return client;
    }

    @Bean
    public RabbitMqQueueReceiver rabbitMqQueueReceiver(RabbitConnectionFactory rabbitConnectionFactory) {
        rabbitConnectionFactory.init();
        RabbitMqQueueReceiver rabbitMqQueueReceiver = new RabbitMqQueueReceiver();
        rabbitMqQueueReceiver.setRabbitConnectionFactory(rabbitConnectionFactory);
        return rabbitMqQueueReceiver;
    }

    @Bean
    public ExecutorRabbitMqQueueReceiver excutorRabbitMqQueueReceiver(RabbitConnectionFactory rabbitConnectionFactory) {
        ExecutorRabbitMqQueueReceiver excutorRabbitMqQueueReceiver = new ExecutorRabbitMqQueueReceiver();
        excutorRabbitMqQueueReceiver.setRabbitConnectionFactory(rabbitConnectionFactory);
        excutorRabbitMqQueueReceiver.setPoolSize(5);
        return excutorRabbitMqQueueReceiver;
    }

    @Bean
    public MqProduceClient mqProduceClient() {
        return new MqProduceClient();
    }
}
