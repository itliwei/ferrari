package com.ziroom.ferrari.task;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.executor.DataChangeMessageExecutor;
import com.ziroom.ferrari.executor.DataChangeMessageSendExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务，未发送和发送失败的
 * @Date 2017/5/2
 */
@Component
public class DataChangeMessageJob {
    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;
    @Autowired
    private DataChangeMessageExecutor dataChangeMessageExecutor;
    //方案一 数据队列
    protected void processSendMsgsJob(List<DataChangeMessageEntity> dataChangeMessageEntities) throws Exception {
        Preconditions.checkNotNull(dataChangeMessageEntities,"dataChangeMessageEntities is null");
        for (DataChangeMessageEntity dataChangeMessageEntity : dataChangeMessageEntities){
            dataChangeMessageSendExecutor.getDataChangeMessageQueue().put(dataChangeMessageEntity);
        }
    }
    //方案二 任务队列
    protected void processSendMsgsJob2(List<DataChangeMessageEntity> dataChangeMessageEntities) throws Exception {
        Preconditions.checkNotNull(dataChangeMessageEntities,"dataChangeMessageEntities is null");
        for (DataChangeMessageEntity dataChangeMessageEntity : dataChangeMessageEntities){
            MessageDataQueue messageDataQueue = dataChangeMessageExecutor.getMessageDataQueue();
            DataChangeMessageQueueTask dataChangeMessageQueueTask = new DataChangeMessageQueueTask(dataChangeMessageExecutor.getDataChangeMessageSendExecutor(),
                    messageDataQueue,dataChangeMessageEntity);
            messageDataQueue.put(dataChangeMessageQueueTask);
        }
    }
}
