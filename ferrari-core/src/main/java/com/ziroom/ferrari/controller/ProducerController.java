package com.ziroom.ferrari.controller;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wangtt
 * @date 2017/5/23 19:13
 * @desc
 * @since 1.0
 */
@RestController
public class ProducerController {

    @RequestMapping(value = "/producer", method = RequestMethod.GET)
    public void produer(){
        DefaultMQProducer producer = new DefaultMQProducer("test_group_name");
        try{
            /**
             * 设置nameserver
             * 默认使用http://jmenv.tbsite.net:8080/rocketmq/nsaddr 自动寻址,只要对应的机器配置hosts即可,开发环境的hosts配置如下
             * 10.16.37.108 jmenv.tbsite.net
             * 也可以通过jvm启动参数配置域名
             * -Drocketmq.namesrv.domain=mynameserver.domain
             * 若都没有配置请使用如下方式设置
             */
            producer.setNamesrvAddr("10.16.37.109:9876");
//        producer.setVipChannelEnabled(false);
            producer.start();

            for (int i = 0; i < 1000; i++) {
                try {
                    Message msg = new Message("TopicTest",// topic
                            "TagA",// tag
                            ("Hello RocketMQ " + i).getBytes()// body
                    );
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(1000);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            producer.shutdown();
        }
    }





}
