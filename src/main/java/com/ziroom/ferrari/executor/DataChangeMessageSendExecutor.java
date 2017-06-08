package com.ziroom.ferrari.executor;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.task.*;
import com.ziroom.ferrari.vo.DataChangeMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 发送任务线程池
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
@Component
public class DataChangeMessageSendExecutor {
    private int threadPoolCount;
    private Map<Integer, ThreadPoolExecutor> executorMap = Maps.newHashMap();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    public DataChangeMessageSendExecutor() {
        this(4);
    }

    public DataChangeMessageSendExecutor(int threadPoolCount) {
        this.threadPoolCount = threadPoolCount;
        for (int i = 0; i < threadPoolCount; i++) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new MessageWorkerQueue());
            executorMap.put(i, threadPoolExecutor);
        }

        //统计线程池
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                taskBacklogStatistics();
            }
        }, 0L, 2L, TimeUnit.MINUTES);
    }

    public void execute(DataChangeMessageEntity dataChangeMessageEntity) {
            int shardingItem = new Long(dataChangeMessageEntity.getChangeKey()).intValue() % threadPoolCount;
            ExecutorService executorService = executorMap.get(shardingItem);
            executorService.execute(new DataChangeMessageWorker(dataChangeMessageEntity));
            log.info("changeKey : " + dataChangeMessageEntity.getChangeKey() + "shardingItem：" +shardingItem);
    }

    private void taskBacklogStatistics() {
        Iterator<Map.Entry<Integer, ThreadPoolExecutor>> iterator = executorMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ThreadPoolExecutor> entry = iterator.next();
            Integer key = entry.getKey();
            ThreadPoolExecutor threadPoolExecutor = entry.getValue();
            log.info("DataChangeMessageSendExecutor线程池：" + key + "当前积压数：" + threadPoolExecutor.getQueue().size());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DataChangeMessageSendExecutor dataChangeMessageSendExecutor = new DataChangeMessageSendExecutor();
        //线程池发送MQ
        for(int i=1;i<100;i++) {
            int r = (int)Math.random()*100;
            DataChangeMessageEntity entity = new DataChangeMessageEntity();
            entity.setMsgFunction("AMS");
            entity.setMsgId(""+i);
            entity.setChangeKey(""+i);
        }

    }

}
