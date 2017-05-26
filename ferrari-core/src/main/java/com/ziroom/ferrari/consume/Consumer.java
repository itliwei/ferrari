package com.ziroom.ferrari.consume;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author wangtt
 * @date 2017/5/23 14:16
 * @desc
 * @since 1.0
 */
public class Consumer {
    public static void main(String[] args) throws InterruptedException, MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer");
        /**
         * 设置nameserver
         * 默认使用http://jmenv.tbsite.net:8080/rocketmq/nsaddr 自动寻址,只要对应的机器配置hosts即可,开发环境的hosts配置如下
         * 10.16.37.108 jmenv.tbsite.net
         * 也可以通过jvm启动参数配置域名
         * -Drocketmq.namesrv.domain=mynameserver.domain
         * 若都没有配置请使用如下方式设置
         */
        consumer.setNamesrvAddr("10.16.37.109:9876");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //设置topic及过滤tag，多个tag使用‘||’分隔
        consumer.subscribe("TopicTest", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();

        System.out.println("Consumer Started.");
    }
}
