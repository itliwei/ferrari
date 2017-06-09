package com.ziroom.ferrari.task;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 发送任务线程池
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
@Component
public class DataChangeMessageSendExecutor {
    private int threadPoolCount = Runtime.getRuntime().availableProcessors();
    private Map<Integer, ThreadPoolExecutor> executorMap = Maps.newHashMap();

    public DataChangeMessageSendExecutor() {
        for (int i = 0; i < threadPoolCount; i++) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new MessageWorkerQueue());//拒绝策略
            executorMap.put(i, threadPoolExecutor);
        }
    }

    public void execute(DataChangeMessageEntity dataChangeMessageEntity) {
        //保证同一房间的消息放到同一队列由同一个线程处理
        int shardingItem = dataChangeMessageEntity.getChangeKey().hashCode() % threadPoolCount;
        ThreadPoolExecutor executorService = executorMap.get(shardingItem);

        executorService.execute(new DataChangeMessageWorker(null, dataChangeMessageEntity));
    }

    public static void main(String[] args) throws InterruptedException {
        DataChangeMessageSendExecutor dataChangeMessageSendExecutor = new DataChangeMessageSendExecutor();
        //线程池发送MQ
        for (int i = 1; i < 100; i++) {
            DataChangeMessageEntity entity = new DataChangeMessageEntity();
            entity.setMsgFunction("AMS");
            entity.setMsgId("" + i);
            entity.setChangeKey("" + i);
            dataChangeMessageSendExecutor.execute(entity);
        }

    }

}
