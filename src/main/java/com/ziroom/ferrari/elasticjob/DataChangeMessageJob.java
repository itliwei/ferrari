package com.ziroom.ferrari.elasticjob;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.task.DataChangeMessageSendExecutor;
import com.ziroom.rent.common.orm.query.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每分钟轮询一次定时任务
 * 基于elasticjob，作为消息可靠性补偿发送，保证消息一定能够发送到MQ
 * @author liwei
 * @date 2017/6/8
 */
@Component
public class DataChangeMessageJob {
    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;
    /**
     * 定时查询未发送的消息
     * @throws Exception
     */
    protected void processUnSendMsgJob() throws Exception {
        List<DataChangeMessageEntity> dataChangeMessageEntities = dataChangeMessageDao.findList(
                Criteria.where("msgStatus", MsgStatusEnum.MSG_UN_SEND.getCode()));
        Preconditions.checkNotNull(dataChangeMessageEntities,"dataChangeMessageEntities is null");
        for (DataChangeMessageEntity dataChangeMessageEntity : dataChangeMessageEntities){
            dataChangeMessageSendExecutor.execute(dataChangeMessageEntity);
        }
    }
}
