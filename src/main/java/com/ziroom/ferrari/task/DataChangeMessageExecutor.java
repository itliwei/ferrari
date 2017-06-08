package com.ziroom.ferrari.task;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author zhoutao
 * @Date 2017/5/5
 */
@Slf4j
public class DataChangeMessageExecutor {
    private int threadPoolCount;
    private Map<Integer, ThreadPoolExecutor> executorMap = Maps.newHashMap();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public DataChangeMessageExecutor() {
        this(4);
    }

    public DataChangeMessageExecutor(int threadPoolCount) {
        this.threadPoolCount = threadPoolCount;
        for (int i = 0; i < threadPoolCount; i++) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new MessageDataQueue());
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

    public void execute(CountDownLatch countDownLatch, String jobName, DataChangeMessageEntity dataChangeMessageEntity) {
        int shardingItem = new Long(dataChangeMessageEntity.getChangeKey()).intValue() % threadPoolCount;
        ExecutorService executorService = executorMap.get(shardingItem);
        executorService.execute(new DataChangeMessageWorker(countDownLatch, jobName, dataChangeMessageEntity));
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


}
