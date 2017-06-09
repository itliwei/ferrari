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
     * @param runnable DataChangeMessageWorker
     * @throws DataChangeMessageSendException
     */
    public void put(Runnable runnable) {
        DataChangeMessageWorker worker = (DataChangeMessageWorker) runnable;
        log.info("MessageWorkerQueue offer :"+worker.getDataChangeMessageEntity());
//        if (this.size()>= DEFAULT_INITIAL_CAPACITY){
////            throw new DataChangeMessageSendException("队列任务数超出最大数");
//            log.error("大于最大线程数："+this.size());
//            return;
//        }
        if(this.keySet.add(worker.getDataChangeMessageEntity().getMsgId())) {
            super.put(worker);
        }
    }
    /**
     * 放置元素
     * @param runnable Runnable
     * @throws DataChangeMessageSendException
     */
    public boolean offer(Runnable runnable) {
        DataChangeMessageWorker worker = (DataChangeMessageWorker) runnable;
        log.info("MessageWorkerQueue offer :"+worker.getDataChangeMessageEntity());
       /* if (this.size()>= DEFAULT_INITIAL_CAPACITY){
//            throw new DataChangeMessageSendException("队列任务数超出最大数");
            log.error("大于最大线程数："+this.size());
            return false;
        }*/
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
        DataChangeMessageWorker dataChangeMessageQueueTask = (DataChangeMessageWorker)super.take();
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
            log.info("MessageWorkerQueue take :"+dataChangeMessageQueueTask.getDataChangeMessageEntity());
        }
        return dataChangeMessageQueueTask;
    }

    public DataChangeMessageWorker poll(long timeout, TimeUnit unit) throws InterruptedException {
        DataChangeMessageWorker dataChangeMessageQueueTask = (DataChangeMessageWorker)super.poll(timeout, unit);
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
            log.info("MessageWorkerQueue poll :"+dataChangeMessageQueueTask.getDataChangeMessageEntity());
        }
        return dataChangeMessageQueueTask;
    }
}
