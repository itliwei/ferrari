package com.ziroom.ferrari.config;

import com.ziroom.ferrari.factory.RabbitFactory;
import com.ziroom.ferrari.producer.DataChangeMessageProducer;
import com.ziroom.ferrari.task.DataChangeMessageSendExecutor;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.ExecutorRabbitMqQueueReceiver;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.RabbitMqQueueReceiver;
import com.ziroom.gaea.mq.rabbitmq.receive.topic.RabbitMqTopicReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${rabbit.server}")
    private String rabbitServer;
    @Value("${rabbit.server.port}")
    private Integer rabbitServerPort;
    @Value("${rabbit.server.username}")
    private String rabbitServerUsername;
    @Value("${rabbit.server.password}")
    private String rabbitServerPassword;
    @Value("${rabbit.server.env}")
    private String rabbitServerEnv;

    //@Bean
    public RabbitConnectionFactory connectionFactory() {
        RabbitConnectionFactory rabbitConnectionFactory = new RabbitConnectionFactory();
        rabbitConnectionFactory.getConnectFactory().setVirtualHost("phoenix");
        return rabbitConnectionFactory;
    }

    @Bean
    public RabbitFactory connectionRabbitFactory() {
        //生产环境去掉队列前缀
        if ("p".equals(rabbitServerEnv)) {
            rabbitServerEnv = null;
        }
        RabbitFactory rabbitFactory = new RabbitFactory(rabbitServer, rabbitServerPort, rabbitServerUsername,
                rabbitServerPassword, rabbitServerEnv);
        rabbitFactory.getConnectFactory().setVirtualHost("phoenix");
        return rabbitFactory;
    }

    @Bean(name = "rabbitMqSendClient")
    public RabbitMqSendClient rabbitMqSendClient(RabbitFactory rabbitFactory) {
        return new RabbitMqSendClient(rabbitFactory);
    }

    @Bean
    public RabbitMqQueueReceiver rabbitMqQueueReceiver(RabbitFactory rabbitConnectionFactory) {
        RabbitMqQueueReceiver rabbitMqQueueReceiver = new RabbitMqQueueReceiver();
        rabbitMqQueueReceiver.setRabbitConnectionFactory(rabbitConnectionFactory);
        return rabbitMqQueueReceiver;
    }

    @Bean
    public RabbitMqTopicReceiver rabbitMqTopicReceiver(RabbitFactory rabbitConnectionFactory) {
        RabbitMqTopicReceiver topicReceiver = new RabbitMqTopicReceiver();
        topicReceiver.setRabbitConnectionFactory(rabbitConnectionFactory);
        return topicReceiver;
    }


    @Bean
    public ExecutorRabbitMqQueueReceiver excutorRabbitMqQueueReceiver(RabbitFactory rabbitConnectionFactory) {
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
