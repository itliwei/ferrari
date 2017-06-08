package com.ziroom.ferrari.task;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.executor.DataChangeMessageSendExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务JOB，未发送和发送失败
 * @Date 2017/6/8
 */
@Component
public class DataChangeMessageJob {
    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;
    //方案二 任务队列
    protected void processUnSendMsgJob(List<DataChangeMessageEntity> dataChangeMessageEntities) throws Exception {
        Preconditions.checkNotNull(dataChangeMessageEntities,"dataChangeMessageEntities is null");
        for (DataChangeMessageEntity dataChangeMessageEntity : dataChangeMessageEntities){
            dataChangeMessageSendExecutor.execute(dataChangeMessageEntity);
        }
    }
}
