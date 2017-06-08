package com.ziroom.ferrari.task;

import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.executor.DataChangeMessageExecutor;
import com.ziroom.ferrari.executor.DataChangeMessageSendExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
@Setter
public class DataChangeMessageQueueTask implements Runnable,Comparable<DataChangeMessageQueueTask> {
    private MessageDataQueue messageDataQueue;

    private DataChangeMessageEntity dataChangeMessageEntity;

    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;

    public DataChangeMessageQueueTask(DataChangeMessageSendExecutor dataChangeMessageSendExecutor,
                                      MessageDataQueue messageDataQueue,DataChangeMessageEntity dataChangeMessageEntity){
        this.dataChangeMessageSendExecutor = dataChangeMessageSendExecutor;
        this.messageDataQueue = messageDataQueue;
        this.dataChangeMessageEntity = dataChangeMessageEntity;
    }
    @Override
    public void run() {
        log.info("put into messageDataQueue:"+dataChangeMessageEntity.toString());
        messageDataQueue.put(this);
        try {
            dataChangeMessageSendExecutor.execute(messageDataQueue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(DataChangeMessageQueueTask o) {
        return this.dataChangeMessageEntity.getChangeTime() > o.dataChangeMessageEntity.getChangeTime()?-1:1;
    }
}
