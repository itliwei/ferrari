package com.ziroom.ferrari.service;

import com.ziroom.busrecoup.IRecoup;
import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.dao.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.task.SendToMqTask;
import com.ziroom.rent.common.application.log.ApplicationLogger;
import com.ziroom.rent.common.orm.query.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 消息重试job
 * Created by liwei on 2017/6/6 0006.
 */
@Slf4j
@Component
public class MessageSendRecoup implements IRecoup {

    @Autowired
    private Executor executorService;
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;

    @Override
    public void recoup(String jobJsonParam, String busCode) throws Exception {
        List<DataChangeMessage> dataChangeMessages = dataChangeMessageDao.findList(Criteria.where("msgStatus",
                MsgStatusEnum.MSG_SEND_FAILURE.getCode()));
        //重试发送失败的任务
        for (DataChangeMessage dataChangeMessage : dataChangeMessages){
            String msgSystem = dataChangeMessage.getMsgSystem();
            String msgModule = dataChangeMessage.getMsgModule();
            String msgFunction = dataChangeMessage.getMsgFunction();
            QueueNameEnum queueNameEnum = QueueNameEnum.getQueueNameEnum(msgSystem, msgModule, msgFunction);

            MessageData messageData = MessageConvert.convertDataChangeMessage(dataChangeMessage);
            SendToMqTask mqTask = new SendToMqTask(queueNameEnum ,messageData,dataChangeMessage);
            executorService.execute(mqTask);
        }
    }

    @Override
    public void afterRecoup(String s, String s1, String s2) throws Exception {
        log.error("Trace data circle life start");
    }
}
