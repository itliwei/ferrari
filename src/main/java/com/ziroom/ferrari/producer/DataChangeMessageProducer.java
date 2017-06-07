package com.ziroom.ferrari.producer;

import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.exception.DataChangeMessageSendException;
import com.ziroom.ferrari.vo.DataChangeMessage;
import com.ziroom.rent.common.idgenerator.ObjectIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by homelink on 2017/5/31 0031.
 */
@Slf4j
@Service
public class DataChangeMessageProducer {
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;

    /**
     * 不真正发送消息到MQ
     * 先持久化到消息DB，再异步发送消息到MQ
     *
     * @param queueNameEnum
     * @param dataChangeMessage
     * @throws DataChangeMessageSendException
     * @author liwei
     */
    public void sendMsg(QueueNameEnum queueNameEnum, DataChangeMessage dataChangeMessage) {
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("DataChangeMessageProducer.sendMsg");
        sb.append("|").append(queueNameEnum).append(",").append(dataChangeMessage);

        try {
            DataChangeMessageEntity dataChangeMessageEntity = MessageConvert.convertMessageData(dataChangeMessage);
            dataChangeMessageEntity.setMsgId(ObjectIdGenerator.nextValue());
            dataChangeMessageEntity.setMsgSystem(queueNameEnum.getSystem());
            dataChangeMessageEntity.setMsgModule(queueNameEnum.getModule());
            dataChangeMessageEntity.setMsgFunction(queueNameEnum.getSystem());
            dataChangeMessageDao.insert(dataChangeMessageEntity);
            sb.append("|Success");
        } catch (RuntimeException e) {
            sb.append("|Failed:" + e.getMessage());
            throw new DataChangeMessageSendException("DataChangeMessageProducer.sendMsg Error", e);
        } finally {
            sb.append("|Time:" + (System.currentTimeMillis() - start));
            log.info(sb.toString());
        }
    }
}
