package com.ziroom.ferrari.convert;

import com.google.common.base.Preconditions;
import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.ferrari.domain.MessageData;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.rent.common.util.DateUtils;

import java.util.Date;

/**
 * 转换类
 * Created by liwei on 2017/6/2 0002.
 */
public class MessageConvert {
    /**
     * 将messageData 转换成 DataChangeMessage
     * @param messageData MessageData
     * @return DataChangeMessage
     */
    public static DataChangeMessage convertMessageData(MessageData messageData){
        Preconditions.checkNotNull(messageData,"messageData 为空");

        DataChangeMessage dataChangeMessage = new DataChangeMessage();
        dataChangeMessage.setMsgId(messageData.getMsgId());
        dataChangeMessage.setProduceTime(DateUtils.format2Long(new Date()));
        dataChangeMessage.setChangeTime(messageData.getChangeTime());
        dataChangeMessage.setChangeData(messageData.getChangeData());
        dataChangeMessage.setChangeEntity(messageData.getChangeEntity());
        dataChangeMessage.setChangeKey(messageData.getChangeKey());
        dataChangeMessage.setChangeType(messageData.getChangeType());
        dataChangeMessage.setMsgStatus(MsgStatusEnum.MSG_UN_SEND.getCode());
        return dataChangeMessage;
    }

    /**
     * dataChangeMessage 转换成 MessageData
     * @param dataChangeMessage DataChangeMessage
     * @return MessageData
     */
    public static  MessageData convertDataChangeMessage(DataChangeMessage dataChangeMessage){
        Preconditions.checkNotNull(dataChangeMessage,"dataChangeMessage 为空");
        MessageData messageData = new MessageData();
        messageData.setId(dataChangeMessage.getId());
        messageData.setMsgId(dataChangeMessage.getMsgId());
        messageData.setProduceTime(DateUtils.format2Long(new Date()));
        messageData.setChangeKey(dataChangeMessage.getChangeKey());
        messageData.setChangeEntity(dataChangeMessage.getChangeEntity());
        messageData.setChangeTime(dataChangeMessage.getChangeTime());
        messageData.setChangeType(dataChangeMessage.getChangeType());
        messageData.setChangeData(dataChangeMessage.getChangeData());
        return messageData;
    }
}
