package com.ziroom.ferrari.task;

import com.ziroom.ferrari.exception.DataChangeMessageSendException;

import java.util.HashSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * MessageDataQueue
 * Created by liwei on 2017/6/7 0007.
 */
public class MessageDataQueue extends PriorityBlockingQueue<Runnable> {

    private transient HashSet<String> keySet = new HashSet<>();
    /**
     * 放置元素
     * @param dataChangeMessageQueueTask
     * @throws DataChangeMessageSendException
     */
    public synchronized void put(DataChangeMessageQueueTask dataChangeMessageQueueTask) throws DataChangeMessageSendException {
        if(this.keySet.add(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId())) {
            super.put(dataChangeMessageQueueTask);
        }
    }
    /**
     * 取出元素
     * @return
     * @throws InterruptedException
     */
    public DataChangeMessageQueueTask take() throws InterruptedException {
        DataChangeMessageQueueTask dataChangeMessageQueueTask = (DataChangeMessageQueueTask)super.take();
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        return dataChangeMessageQueueTask;
    }

    public DataChangeMessageQueueTask poll(long timeout, TimeUnit unit) throws InterruptedException {
        DataChangeMessageQueueTask dataChangeMessageQueueTask = (DataChangeMessageQueueTask)super.poll(timeout, unit);
        if(dataChangeMessageQueueTask != null) {
            this.keySet.remove(dataChangeMessageQueueTask.getDataChangeMessageEntity().getMsgId());
        }
        return dataChangeMessageQueueTask;
    }
}
