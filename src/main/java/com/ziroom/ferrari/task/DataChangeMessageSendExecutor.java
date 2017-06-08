package com.ziroom.ferrari.task;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
public class DataChangeMessageSendExecutor {
    private int threadPoolCount;
    private Map<Integer, ThreadPoolExecutor> executorMap = Maps.newHashMap();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private DataChangeMessageQueue dataChangeMessageQueue = new DataChangeMessageQueue();

    public DataChangeMessageSendExecutor() {
        this(4);
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
        while (true){
            DataChangeMessageEntity take = dataChangeMessageQueue.take();
            int shardingItem = new Long(take.getChangeKey()).intValue() % threadPoolCount;
            ExecutorService executorService = executorMap.get(shardingItem);
            executorService.execute(new DataChangeMessageWorker(""+shardingItem, take));
            log.info("changeKey : "+take.getChangeKey()+"shardingItem：" + shardingItem + "当前积压数：" + dataChangeMessageQueue.size());
        }
    }

    private void taskBacklogStatistics() {
        Iterator<Map.Entry<Integer, ThreadPoolExecutor>> iterator = executorMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, ThreadPoolExecutor> entry = iterator.next();
            Integer key = entry.getKey();
            ThreadPoolExecutor threadPoolExecutor = entry.getValue();
            log.info("SolrRoomIncrWorkExecuter线程池：" + key + "当前积压数：" + threadPoolExecutor.getQueue().size());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DataChangeMessageSendExecutor dataChangeMessageSendExecutor = new DataChangeMessageSendExecutor();
        DataChangeMessageQueueSendTask dataChangeMessageQueueTask = new DataChangeMessageQueueSendTask(dataChangeMessageSendExecutor);
        Thread thread = new Thread(dataChangeMessageQueueTask);
        thread.start();
        //线程池发送MQ
        while(true) {
            Random random = new Random(10000);
            DataChangeMessageEntity entity = new DataChangeMessageEntity();
            entity.setMsgFunction("AMS");
            entity.setMsgId(""+random);
            entity.setChangeKey(""+random);
            dataChangeMessageSendExecutor.dataChangeMessageQueue.put(entity);
        }

    }

}
