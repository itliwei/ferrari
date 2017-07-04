package com.ziroom.ferrari.producer;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.convert.MessageConvert;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.ChangeTypeEnum;
import com.ziroom.ferrari.enums.QueueNameEnum;
import com.ziroom.ferrari.exception.DataChangeMessageSendException;
import com.ziroom.ferrari.task.DataChangeMessageSendExecutor;
import com.ziroom.ferrari.vo.DataChangeMessage;
import com.ziroom.gaea.mq.rabbitmq.entity.QueueName;
import com.ziroom.rent.common.idgenerator.ObjectIdGenerator;
import com.ziroom.rent.common.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by homelink on 2017/5/31 0031.
 */
@Getter
@Setter
@Slf4j
@Service(value = "com.ziroom.ferrari.producer.DataChangeMessageProducer")
public class DataChangeMessageProducer {
    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;
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
        Preconditions.checkNotNull(queueNameEnum, "queueNameEnum 不能为空");
        Preconditions.checkNotNull(dataChangeMessage, "dataChangeMessage 不能为空");
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("DataChangeMessageProducer.sendMsg");
        sb.append("|").append(queueNameEnum).append(",").append(dataChangeMessage);
        sendMessageEntity(queueNameEnum.getSystem(),queueNameEnum.getModule(),queueNameEnum.getFunction(),
                dataChangeMessage, start, sb);
    }

    /**
     * 不真正发送消息到MQ
     * 先持久化到消息DB，再异步发送消息到MQ
     *
     * @param queueName QueueName
     * @param dataChangeMessage
     * @throws DataChangeMessageSendException
     * @author liwei
     */
    public void sendMsg(QueueName queueName, DataChangeMessage dataChangeMessage) {
        Preconditions.checkNotNull(queueName, "queueNameEnum 不能为空");
        Preconditions.checkNotNull(dataChangeMessage, "dataChangeMessage 不能为空");
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("DataChangeMessageProducer.sendMsg");
        sb.append("|").append(queueName.toString()).append(",").append(dataChangeMessage);
        sendMessageEntity(queueName.getSystem(),queueName.getModule(),queueName.getFunction(),
                dataChangeMessage, start, sb);
    }

    /**
     * 1、封装DataChangeMessageEntity
     * 2、插入数据库
     * 3、放入线程池
     * @param system
     * @param module
     * @param function
     * @param dataChangeMessage
     * @param start
     * @param sb
     */
    private void sendMessageEntity(String system,String module,String function,
                                   DataChangeMessage dataChangeMessage, long start, StringBuilder sb) {
        //FIXME 1.0.1.1-SNAPSHOT这个版本什么都不做
//        try {
//            DataChangeMessageEntity dataChangeMessageEntity = MessageConvert.convertDataChangeMessage(dataChangeMessage);
//            //生产msgId
//            dataChangeMessageEntity.setMsgId(ObjectIdGenerator.nextValue());
//            dataChangeMessageEntity.setMsgSystem(system);
//            dataChangeMessageEntity.setMsgModule(module);
//            dataChangeMessageEntity.setMsgFunction(function);
//            dataChangeMessageDao.insert(dataChangeMessageEntity);
//            sb.append("|插入数据库:success");
//            //发送任务
//            dataChangeMessageSendExecutor.execute(dataChangeMessageEntity);
//            sb.append("|发送任务:Success");
//        } catch (RuntimeException e) {
//            sb.append("|发送任务:Failed:" + e.getMessage());
//            throw new DataChangeMessageSendException("DataChangeMessageProducer.sendMsg Error", e);
//        } finally {
//            sb.append("|Time:" + (System.currentTimeMillis() - start));
//            log.info(sb.toString());
//        }
    }

}
