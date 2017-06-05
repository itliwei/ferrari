package com.ziroom.ferrari.produce;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.dao.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.service.MqProduceService;
import com.ziroom.ferrari.service.MqProduceServiceImpl;
import com.ziroom.ferrari.task.SendToMqTask;
import com.ziroom.rent.common.idgenerator.ObjectIdGenerator;
import com.ziroom.rent.common.orm.query.Criteria;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by homelink on 2017/5/31 0031.
 */
@Getter
@Slf4j
@Service
public class MqProduceClient {
    BlockingQueue<MessageData> retryQueue = new LinkedBlockingQueue<>();
    @Autowired
    private MqProduceService mqProduceService;
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;
    @Autowired
    private ExecutorService commonExecutorService;
    //枚举类
    private QueueNameEnum queueNameEnum;

    public MqProduceClient() {
        if(mqProduceService == null){
            mqProduceService = new MqProduceServiceImpl();
        }
    }

    /**
     * 发送消息
     * @author liwei
     * @param queueNameEnum MessageData
     * @param messageData QueueNameEnum
     */
    public int sendMsg(QueueNameEnum queueNameEnum ,MessageData messageData) {
        this.queueNameEnum = queueNameEnum;
        Preconditions.checkNotNull(queueNameEnum,"queueNameEnum 为空");
        Preconditions.checkNotNull(messageData,"messageData 为空");

        //生产msgId
        messageData.setMsgId(ObjectIdGenerator.nextValue());
        DataChangeMessage dataChangeMessage = MessageConvert.convertMessageData(messageData);

        dataChangeMessage.setMsgSystem(queueNameEnum.getSystem());
        dataChangeMessage.setMsgModule(queueNameEnum.getModule());
        dataChangeMessage.setMsgFunction(queueNameEnum.getSystem());
        dataChangeMessageDao.insert(dataChangeMessage);
        //线程池发送MQ
        SendToMqTask mqTask = new SendToMqTask(this ,messageData,dataChangeMessage);
        commonExecutorService.execute(mqTask);
        return 1;
    }

    /**
     * 每30s 重试一下未发送的
     */
    @Scheduled(fixedRate =  30 * 1000L)
    public void retrySendFailure() {
        List<DataChangeMessage> dataChangeMessages = dataChangeMessageDao.findList(Criteria.where("msgStatus",
                MsgStatusEnum.MSG_SEND_FAILURE.getCode()));
        for (DataChangeMessage dataChangeMessage : dataChangeMessages){
            MessageData messageData = MessageConvert.convertDataChangeMessage(dataChangeMessage);

            SendToMqTask mqTask = new SendToMqTask(this ,messageData,dataChangeMessage);
            commonExecutorService.execute(mqTask);
        }
    }
}
