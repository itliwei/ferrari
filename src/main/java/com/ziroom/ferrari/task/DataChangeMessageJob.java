package com.ziroom.ferrari.task;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.executor.DataChangeMessageSendExecutor;
import com.ziroom.rent.common.orm.query.Criteria;
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
