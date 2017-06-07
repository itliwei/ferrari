package com.ziroom.ferrari.convert;

import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.vo.DataChangeMessage;
import com.ziroom.rent.common.util.DateUtils;

import java.util.Date;

/**
 * 转换类
 * Created by liwei on 2017/6/2 0002.
 */
public class MessageConvert {
    /**
     * 将messageData 转换成 DataChangeMessageEntity
     *
     * @param dataChangeMessage MessageData
     * @return DataChangeMessage
     */
    public static DataChangeMessageEntity convertMessageData(DataChangeMessage dataChangeMessage) {
        DataChangeMessageEntity dataChangeMessageEntity = new DataChangeMessageEntity();
        dataChangeMessageEntity.setProduceTime(DateUtils.format2Long(new Date()));
        dataChangeMessageEntity.setChangeTime(dataChangeMessage.getChangeTime());
        dataChangeMessageEntity.setChangeData(dataChangeMessage.getChangeData());
        dataChangeMessageEntity.setChangeEntityName(dataChangeMessage.getChangeEntityName());
        dataChangeMessageEntity.setChangeKey(dataChangeMessage.getChangeKey());
        dataChangeMessageEntity.setChangeType(dataChangeMessage.getChangeType().getCode());
        dataChangeMessageEntity.setMsgStatus(MsgStatusEnum.MSG_UN_SEND.getCode());
        return dataChangeMessageEntity;
    }
}
