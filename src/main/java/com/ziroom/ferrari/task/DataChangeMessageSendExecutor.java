package com.ziroom.ferrari.task;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.gaea.mq.rabbitmq.client.RabbitMqSendClient;
import com.ziroom.rent.common.application.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 发送任务线程池
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Component
public class DataChangeMessageSendExecutor {
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;
    @Autowired
    private RabbitMqSendClient rabbitMqSendClient;

    private int threadPoolCount = Runtime.getRuntime().availableProcessors();

    private Map<Integer, ThreadPoolExecutor> executorMap = Maps.newHashMap();

    public DataChangeMessageSendExecutor() {
        for (int i = 0; i < threadPoolCount; i++) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new MessageWorkerQueue(), new NamedThreadFactory("Ferrari"));
            executorMap.put(i, threadPoolExecutor);
        }
    }

    public void execute(DataChangeMessageEntity dataChangeMessageEntity) {
        //保证同一房间的消息放到同一队列由同一个线程处理
        int shardingItem = dataChangeMessageEntity.getChangeKey().hashCode() % threadPoolCount;
        ThreadPoolExecutor executorService = executorMap.get(Math.abs(shardingItem));

        executorService.execute(new DataChangeMessageWorker(dataChangeMessageEntity, rabbitMqSendClient, dataChangeMessageDao));
    }

}
