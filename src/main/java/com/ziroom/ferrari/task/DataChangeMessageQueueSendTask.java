package com.ziroom.ferrari.task;

import com.ziroom.ferrari.executor.DataChangeMessageSendExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
@Setter
public class DataChangeMessageQueueSendTask implements Runnable{
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;

    public DataChangeMessageQueueSendTask(DataChangeMessageSendExecutor dataChangeMessageSendExecutor){
        this.dataChangeMessageSendExecutor = dataChangeMessageSendExecutor;
    }
    @Override
    public void run() {
        try {
            dataChangeMessageSendExecutor.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
