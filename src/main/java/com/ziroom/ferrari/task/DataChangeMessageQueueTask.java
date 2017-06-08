package com.ziroom.ferrari.task;

import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
@Setter
public class DataChangeMessageQueueTask implements Runnable,Comparable<DataChangeMessageQueueTask> {
    private MessageDataQueue messageDataQueue;

    private DataChangeMessageEntity dataChangeMessageEntity;

    public DataChangeMessageQueueTask(MessageDataQueue messageDataQueue,DataChangeMessageEntity dataChangeMessageEntity){
        this.messageDataQueue = messageDataQueue;
        this.dataChangeMessageEntity = dataChangeMessageEntity;
    }
    @Override
    public void run() {
        log.info("put into messageDataQueue:"+dataChangeMessageEntity.toString());
        messageDataQueue.put(this);
    }

    @Override
    public int compareTo(DataChangeMessageQueueTask o) {
        return this.dataChangeMessageEntity.getChangeTime() > o.dataChangeMessageEntity.getChangeTime()?-1:1;
    }
}
