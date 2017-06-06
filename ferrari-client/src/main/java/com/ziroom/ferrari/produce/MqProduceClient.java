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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;


/**
 * Created by homelink on 2017/5/31 0031.
 */
@Getter
@Setter
@Slf4j
@Service
public class MqProduceClient {
    @Autowired
    private MqProduceService mqProduceService;
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;
    @Autowired
    private Executor executorService ;
    //枚举类
    private QueueNameEnum queueNameEnum;

    public MqProduceClient(){

    }

    public MqProduceClient( DataChangeMessageDao dataChangeMessageDao) {

        if (mqProduceService == null){
            this.mqProduceService = new MqProduceServiceImpl();
        }
        this.dataChangeMessageDao = dataChangeMessageDao;
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
        SendToMqTask mqTask = new SendToMqTask(queueNameEnum ,messageData,dataChangeMessage);
        executorService.execute(mqTask);
        return 1;
    }
}
