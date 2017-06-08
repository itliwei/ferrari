package com.ziroom.ferrari.executor;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.task.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
@Component
public class DataChangeMessageSendExecutor {
    private int threadPoolCount;
    private Map<Integer, ThreadPoolExecutor> executorMap = Maps.newHashMap();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private DataChangeMessageQueue dataChangeMessageQueue = new DataChangeMessageQueue();

    public DataChangeMessageSendExecutor() {
        this(4);
        //启动监控任务线程
        DataChangeMessageQueueSendTask dataChangeMessageQueueTask = new DataChangeMessageQueueSendTask(this);
        Thread thread = new Thread(dataChangeMessageQueueTask);
        thread.start();
    }

    public DataChangeMessageSendExecutor(int threadPoolCount) {
        this.threadPoolCount = threadPoolCount;
        for (int i = 0; i < threadPoolCount; i++) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue());
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

    public void execute() throws InterruptedException {
        while (!dataChangeMessageQueue.isEmpty()){
            DataChangeMessageEntity take = dataChangeMessageQueue.take();
            int shardingItem = new Long(take.getChangeKey()).intValue() % threadPoolCount;
            ExecutorService executorService = executorMap.get(shardingItem);
            executorService.execute(new DataChangeMessageWorker(""+shardingItem, take));
            log.info("changeKey : "+take.getChangeKey()+"shardingItem：" + shardingItem + "当前积压数：" + dataChangeMessageQueue.size());
        }
    }

    public void execute(MessageDataQueue messageDataQueue) throws InterruptedException {
        while (!messageDataQueue.isEmpty()) {
            DataChangeMessageQueueTask take = messageDataQueue.take();
            DataChangeMessageEntity dataChangeMessageEntity = take.getDataChangeMessageEntity();
            int shardingItem = new Long(dataChangeMessageEntity.getChangeKey()).intValue() % threadPoolCount;
            ExecutorService executorService = executorMap.get(shardingItem);
            executorService.execute(new DataChangeMessageWorker("" + shardingItem, dataChangeMessageEntity));
            log.info("changeKey : " + dataChangeMessageEntity.getChangeKey() + "shardingItem：" + shardingItem + "当前积压数：" + dataChangeMessageQueue.size());
        }
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
            dataChangeMessageSendExecutor.dataChangeMessageQueue.put(entity);
        }

    }

}
