package com.ziroom.ferrari.task;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @Date 2017/5/2
 */
@Component
public class DataChangeMessageJob {

    private static final String JOB_NAME = "DataChangeMessage job ";

    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor = new DataChangeMessageSendExecutor();
    @Autowired
    private DataChangeMessageExecutor dataChangeMessageExecutor;

    protected void processMyShardDatas(List<DataChangeMessageEntity> dataChangeMessageEntities) throws Exception {
        Preconditions.checkNotNull(dataChangeMessageEntities,"dataChangeMessageEntities is null");
        CountDownLatch countDownLatch = new CountDownLatch(dataChangeMessageEntities.size());
        MessageDataQueue msgDataQueue = (MessageDataQueue)dataChangeMessageExecutor.dataChangeMessageExecutor.getQueue();
        while (msgDataQueue!= null){
            DataChangeMessageQueueTask take = msgDataQueue.take();
            if (take != null) {
                DataChangeMessageEntity dataChangeMessageEntity = take.getDataChangeMessageEntity();
                dataChangeMessageSendExecutor.execute(countDownLatch, JOB_NAME, dataChangeMessageEntity);
            }
        }
        countDownLatch.await();
    }
}
