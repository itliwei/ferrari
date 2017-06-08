package com.ziroom.ferrari.task;

import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.exception.DataChangeMessageSendException;

import java.util.HashSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by homelink on 2017/6/8 0008.
 */
public class DataChangeMessageQueue extends PriorityBlockingQueue<DataChangeMessageEntity> {
    private transient HashSet<String> keySet = new HashSet<>();
    /**
     * 放置元素
     * @param dataChangeMessageEntity dataChangeMessageEntity
     * @throws DataChangeMessageSendException
     */
    public synchronized void put(DataChangeMessageEntity dataChangeMessageEntity) throws DataChangeMessageSendException {
        if(this.keySet.add(dataChangeMessageEntity.getMsgId())) {
            super.put(dataChangeMessageEntity);
        }
    }
    /**
     * 取出元素
     * @return dataChangeMessageEntity
     * @throws InterruptedException
     */
    public DataChangeMessageEntity take() throws InterruptedException {
        DataChangeMessageEntity dataChangeMessageEntity = super.take();
        if(dataChangeMessageEntity != null) {
            this.keySet.remove(dataChangeMessageEntity.getMsgId());
        }
        return dataChangeMessageEntity;
    }

    public DataChangeMessageEntity poll(long timeout, TimeUnit unit) throws InterruptedException {
        DataChangeMessageEntity dataChangeMessageEntity = super.poll(timeout, unit);
        if(dataChangeMessageEntity != null) {
            this.keySet.remove(dataChangeMessageEntity.getMsgId());
        }
        return dataChangeMessageEntity;
    }
}
