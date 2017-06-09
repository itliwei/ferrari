package com.ziroom.ferrari.task;

import com.ziroom.ferrari.exception.DataChangeMessageSendException;

import java.util.HashSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * MessageDataQueue
 * Created by liwei on 2017/6/7 0007.
 */
public class MessageWorkerQueue extends PriorityBlockingQueue<Runnable> {

    private transient HashSet<String> keySet = new HashSet<>();

    public MessageWorkerQueue(int initialCapacity){

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
     * 取出元素
     * @return
     * @throws InterruptedException
     */
    public DataChangeMessageWorker take() throws InterruptedException {
        DataChangeMessageWorker dataChangeMessageQueueTask = (DataChangeMessageWorker)super.take();
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        return dataChangeMessageQueueTask;
    }

    public DataChangeMessageWorker poll(long timeout, TimeUnit unit) throws InterruptedException {
        DataChangeMessageWorker dataChangeMessageQueueTask = (DataChangeMessageWorker)super.poll(timeout, unit);
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        return dataChangeMessageQueueTask;
    }
}
