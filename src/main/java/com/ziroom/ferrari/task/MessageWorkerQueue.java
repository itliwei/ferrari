package com.ziroom.ferrari.task;

import com.ziroom.ferrari.exception.DataChangeMessageSendException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * MessageDataQueue
 * Created by liwei on 2017/6/7 0007.
 */
@Slf4j
public class MessageWorkerQueue extends PriorityBlockingQueue<DataChangeMessageWorker> {
    private static final int DEFAULT_INITIAL_CAPACITY = 20;

    private transient HashSet<String> keySet = new HashSet<>();

    public MessageWorkerQueue(){
        this(DEFAULT_INITIAL_CAPACITY);
        log.info("init MessageWorkerQueue ");
    }
    public MessageWorkerQueue(int initialCapacity){
        super(initialCapacity);
    }
    /**
     * 放置元素
     * @param worker DataChangeMessageWorker
     * @throws DataChangeMessageSendException
     */
    public void put(DataChangeMessageWorker worker) {
        if(this.keySet.add(worker.getDataChangeMessageEntity().getMsgId())) {
            super.put(worker);
        }
    }
    /**
     * 放置元素
     * @param worker DataChangeMessageWorker
     * @throws DataChangeMessageSendException
     */
    public boolean offer(DataChangeMessageWorker worker) {
        log.info("MessageWorkerQueue offer :"+worker.getDataChangeMessageEntity());
        if (this.size()>DEFAULT_INITIAL_CAPACITY){
            throw new DataChangeMessageSendException("队列任务数超出最大数");
        }
        if(this.keySet.add(worker.getDataChangeMessageEntity().getMsgId())) {
            super.offer(worker);
            return  true;
        }
        return false;
    }
    /**
     * 取出元素
     * @return
     * @throws InterruptedException
     */
    public DataChangeMessageWorker take() throws InterruptedException {
        DataChangeMessageWorker dataChangeMessageQueueTask = super.take();
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        log.info("MessageWorkerQueue take :"+dataChangeMessageQueueTask.getDataChangeMessageEntity());
        return dataChangeMessageQueueTask;
    }

    public DataChangeMessageWorker poll(long timeout, TimeUnit unit) throws InterruptedException {
        DataChangeMessageWorker dataChangeMessageQueueTask = super.poll(timeout, unit);
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        return dataChangeMessageQueueTask;
    }
}
