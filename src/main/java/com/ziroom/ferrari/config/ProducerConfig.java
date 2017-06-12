package com.ziroom.ferrari.config;

import com.ziroom.ferrari.producer.DataChangeMessageProducer;
import com.ziroom.ferrari.task.DataChangeMessageSendExecutor;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.ExecutorRabbitMqQueueReceiver;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.RabbitMqQueueReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * dataChangeMessageProducer 注入bean
 *
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Configuration
public class ProducerConfig {

    @Bean
    public RabbitConnectionFactory connectionFactory() {
        return new RabbitConnectionFactory();
    }

    @Bean(name = "rabbitMqSendClient")
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

    @Bean(name = "dataChangeMessageProducer")
    public DataChangeMessageProducer mqProduceClient() {
        return new DataChangeMessageProducer();
    }

    @Bean(name = "dataChangeMessageSendExecutor")
    public DataChangeMessageSendExecutor dataChangeMessageSendExecutor() {
        return new DataChangeMessageSendExecutor();
    }


}
