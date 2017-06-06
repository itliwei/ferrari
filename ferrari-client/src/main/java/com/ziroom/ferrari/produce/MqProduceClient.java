package com.ziroom.ferrari.produce;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.dao.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.enums.ResultEnum;
import com.ziroom.ferrari.result.BaseResult;
import com.ziroom.ferrari.service.DataChangeMessageService;
import com.ziroom.ferrari.service.MqProduceService;
import com.ziroom.ferrari.service.impl.DataChangeMessageServiceImpl;
import com.ziroom.ferrari.service.impl.MqProduceServiceImpl;
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
    private DataChangeMessageService dataChangeMessageService;
    @Autowired
    private Executor executorService ;

    /**
     * 发送消息
     * @author liwei
     * @param queueNameEnum MessageData
     * @param messageData QueueNameEnum
     */
    public BaseResult sendMsg(QueueNameEnum queueNameEnum , MessageData messageData) {
        log.info("MqProduceClient sendMsg :" +messageData.toString());
        Preconditions.checkNotNull(queueNameEnum,"queueNameEnum 为空");
        Preconditions.checkNotNull(messageData,"messageData 为空");
        BaseResult baseResult;
        //生产msgId
        messageData.setMsgId(ObjectIdGenerator.nextValue());
        DataChangeMessage dataChangeMessage = MessageConvert.convertMessageData(messageData);
        dataChangeMessage.setMsgSystem(queueNameEnum.getSystem());
        dataChangeMessage.setMsgModule(queueNameEnum.getModule());
        dataChangeMessage.setMsgFunction(queueNameEnum.getSystem());
        //插入数据库
        int count = dataChangeMessageService.insert(dataChangeMessage);
        //插入成功
        if(count > 0) {
            //线程池发送MQ
            SendToMqTask mqTask = new SendToMqTask(queueNameEnum, messageData, dataChangeMessage);
            executorService.execute(mqTask);
            baseResult = new BaseResult(ResultEnum.SUCCESS);
        }else{
            baseResult = new BaseResult(ResultEnum.FAILURE);
            baseResult.setMessage("插入数据库失败");
        }
        return baseResult;
    }
}
