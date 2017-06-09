package com.ziroom.ferrari.convert;

import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.ChangeTypeEnum;
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
    public static DataChangeMessageEntity convertDataChangeMessage(DataChangeMessage dataChangeMessage) {
        if (dataChangeMessage == null){
            return null;
        }

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

    /**
     * dataChangeMessageEntity 转为 DataChangeMessage
     * @param dataChangeMessageEntity
     * @return
     */
    public static DataChangeMessage convertDataChangeMessageEntity(DataChangeMessageEntity dataChangeMessageEntity) {
        if (dataChangeMessageEntity == null){
            return null;
        }
        DataChangeMessage dataChangeMessage = new DataChangeMessage();
        dataChangeMessage.setChangeTime(dataChangeMessageEntity.getChangeTime());
        dataChangeMessage.setChangeData(dataChangeMessageEntity.getChangeData());
        dataChangeMessage.setChangeEntityName(dataChangeMessageEntity.getChangeEntityName());
        dataChangeMessage.setChangeKey(dataChangeMessageEntity.getChangeKey());
        dataChangeMessage.setChangeType(ChangeTypeEnum.getByCode(dataChangeMessageEntity.getChangeType()));

        return dataChangeMessage;
    }
}
