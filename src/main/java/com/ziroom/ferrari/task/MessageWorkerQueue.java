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
public class MessageWorkerQueue extends PriorityBlockingQueue<Runnable> {
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
     * @param dataChangeMessageWorker
     * @throws DataChangeMessageSendException
     */
    public synchronized void put(DataChangeMessageWorker dataChangeMessageWorker) throws DataChangeMessageSendException {
        if(this.keySet.add(dataChangeMessageWorker.getDataChangeMessageEntity().getMsgId())) {
            super.put(dataChangeMessageWorker);
        }
    }
    /**
     * 放置元素
     * @param dataChangeMessageWorker
     * @throws DataChangeMessageSendException
     */
    public synchronized void offer(DataChangeMessageWorker dataChangeMessageWorker) throws DataChangeMessageSendException {
        log.info("MessageWorkerQueue offer :"+dataChangeMessageWorker.toString());
        if (this.size()>DEFAULT_INITIAL_CAPACITY){
            throw new DataChangeMessageSendException("队列任务数超出最大数");
        }
        if(this.keySet.add(dataChangeMessageWorker.getDataChangeMessageEntity().getMsgId())) {
            super.offer(dataChangeMessageWorker);
        }
    }
    /**
     * 取出元素
     * @return
     * @throws InterruptedException
     */
    public DataChangeMessageWorker take() throws InterruptedException {
        log.info("MessageWorkerQueue take ");
        DataChangeMessageWorker dataChangeMessageQueueTask = (DataChangeMessageWorker)super.take();
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        return dataChangeMessageQueueTask;
    }

    public DataChangeMessageWorker poll(long timeout, TimeUnit unit) throws InterruptedException {
        log.info("MessageWorkerQueue pool ");
        DataChangeMessageWorker dataChangeMessageQueueTask = (DataChangeMessageWorker)super.poll(timeout, unit);
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        return dataChangeMessageQueueTask;
    }
}
