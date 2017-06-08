package com.ziroom.ferrari.task;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Date 2017/5/2
 */
@Component
public class DataChangeMessageJob {
    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;

    protected void processSendMsgsJob(List<DataChangeMessageEntity> dataChangeMessageEntities) throws Exception {
        Preconditions.checkNotNull(dataChangeMessageEntities,"dataChangeMessageEntities is null");

        for (DataChangeMessageEntity dataChangeMessageEntity : dataChangeMessageEntities){
            dataChangeMessageSendExecutor.getDataChangeMessageQueue().put(dataChangeMessageEntity);
        }
    }
}
