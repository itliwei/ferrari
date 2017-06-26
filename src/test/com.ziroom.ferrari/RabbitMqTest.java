package com.ziroom.ferrari;

import com.rabbitmq.client.*;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.factory.RabbitFactory;
import com.ziroom.gaea.mq.rabbitmq.PublishSubscribeType;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.gaea.mq.rabbitmq.entity.BindingKey;
import com.ziroom.gaea.mq.rabbitmq.entity.ExchangeName;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.gaea.mq.rabbitmq.entity.RoutingKey;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import com.ziroom.gaea.mq.rabbitmq.receive.RabbitMqMessageListener;
import com.ziroom.gaea.mq.rabbitmq.receive.queue.RabbitMqQueueReceiver;
import com.ziroom.gaea.mq.rabbitmq.receive.topic.RabbitMqTopicReceiver;
import com.ziroom.rent.common.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**\
 * 自定义 RabbitFactory
 * Created by homelink on 2017/6/13 0013.
 */
public class RabbitMqTest {

   public static void main(String[] args) {
       RabbitFactory rabbitConnectionFactory = new RabbitFactory("10.16.9.34,10.16.9.35,10.16.9.37",
               5672,"phoenix_rabbit_write",
               "phoenix_rabbit_write","d");

       rabbitConnectionFactory.getConnectFactory().setVirtualHost("phoenix");
       RabbitMqSendClient rabbitMqSendClient = new RabbitMqSendClient(rabbitConnectionFactory);
       RabbitMqQueueReceiver rabbitMqQueueReceiver = new RabbitMqQueueReceiver(rabbitConnectionFactory);
       ExchangeName exchangeName = new ExchangeName("test",
               "liwei",QueueNameEnum.INVENTORY.getFunction());
       RoutingKey routingKey = new RoutingKey("test",
               "liwei",QueueNameEnum.INVENTORY.getFunction());
       QueueName queueName = new QueueName("test",
               "liwei",QueueNameEnum.INVENTORY.getFunction());
       BindingKey bindingKey = new BindingKey("test",
               "*",QueueNameEnum.INVENTORY.getFunction());

       BindingKey bindingKey2 = new BindingKey(QueueNameEnum.INVENTORY.getSystem(),
               "*",QueueNameEnum.INVENTORY.getFunction());

       List<RabbitMqMessageListener> listeners = new ArrayList<>(1);
       listeners.add(new RabbitMqMessageListener() {
           @Override
           public void processMessage(QueueingConsumer.Delivery delivery) throws Exception {
               System.out.println("consumer msg :"+delivery.getEnvelope().getRoutingKey()+"|||"+new String(delivery.getBody()));
           }
       });

       List<RabbitMqMessageListener> listeners2 = new ArrayList<>(1);
       listeners.add(new RabbitMqMessageListener() {
           @Override
           public void processMessage(QueueingConsumer.Delivery delivery) throws Exception {
               System.out.println("listeners2 consumer msg :"+delivery.getEnvelope().getRoutingKey()+"|||"+new String(delivery.getBody()));
           }
       });

       RabbitMqTopicReceiver topicReceiver = new RabbitMqTopicReceiver(rabbitConnectionFactory,listeners,
               queueName,bindingKey,exchangeName,PublishSubscribeType.TOPIC);

       RabbitMqTopicReceiver topicReceiver2 = new RabbitMqTopicReceiver(rabbitConnectionFactory,listeners,
               queueName,bindingKey,exchangeName,PublishSubscribeType.TOPIC);

       RabbitMqQueueReceiver queueReceiver = new RabbitMqQueueReceiver(rabbitConnectionFactory, listeners, queueName);
       new Thread(){
           @Override
           public void run() {
              for (int i=0;i<10;i++){
                   try {
                       TimeUnit.MILLISECONDS.sleep(500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   String msg = DateUtils.currentTimeMillis();
                   System.out.println("send msg:" + msg);
                   try {
//                       rabbitMqSendClient.sendQueue(queueName,msg);
//                       rabbitMqSendClient.sendTopic(exchangeName, routingKey, PublishSubscribeType.TOPIC,  msg);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   System.out.println("send msg success !!!");
               }
           }
       }.start();

       /*new Thread(){
           @Override
           public void run() {
               try {
                   TimeUnit.MILLISECONDS.sleep(80);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

               try {
//                       queueReceiver.receiveMessage();
                   topicReceiver.receiveMessage();
                   topicReceiver2.receiveMessage();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }.start();*/

   }
}
