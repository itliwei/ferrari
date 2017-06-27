package com.ziroom.ferrari.elasticjob;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.exception.JobException;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.google.common.collect.Lists;
import com.ziroom.ferrari.domain.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.enums.MsgStatusEnum;
import com.ziroom.ferrari.task.DataChangeMessageSendExecutor;
import com.ziroom.rent.common.constant.SymbolConstant;
import com.ziroom.rent.common.orm.query.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

import java.util.List;

/**
 * 消息发送补偿作业
 * 基于elasticjob，作为消息可靠性补偿发送，保证消息一定能够发送到MQ
 *
 * @author liwei
 * @date 2017/6/8
 */
@Slf4j
@Component
public class MsgSendRecoupJob extends AbstractSimpleElasticJob {
    public static final String JOB_NAME = "msgSendRecoupJob";
    public static final String JOB_DESC = "msgSendRecoupJob[DB拉取消息发送到MQ补偿作业]";

    @Autowired
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;

    @Override
    public void handleJobExecutionException(final JobException jobException) {
        log.error("DataChangeMessageJob.process error", jobException);
    }

    @Override
    public void process(final JobExecutionMultipleShardingContext context) {
        StringBuilder logSB = new StringBuilder();
        long start = System.currentTimeMillis();
        logSB.append(JOB_DESC + " 开始");

        //得到分片项
        List<Integer> shardingItems = context.getShardingItems();
        if (CollectionUtils.isEmpty(shardingItems)) {
            logSB.append("|该服务器未分配到分片项，放弃执行");
            log.warn(logSB.toString());
            return;
        }
        logSB.append("|得到的分片项：" + shardingItems.toString()).append(SymbolConstant.BAR);

        //得到所有待处理任务
        List<DataChangeMessageEntity> allShardDatas;
        try {
            allShardDatas = getAllShardDatas();
            if (CollectionUtils.isEmpty(allShardDatas)) {
                logSB.append("|没有待发送的消息，放弃执行");
                log.warn(logSB.toString());
                return;
            }
        } catch (RuntimeException e) {
            logSB.append("|getAllShardDatas Exception:" + e.getMessage());
            log.error(logSB.toString());
            return;
        }

        logSB.append("|所有分片待发送的消息数量:" + allShardDatas.size()).append(SymbolConstant.BAR);


        //得到该实际待处理数据
        List<DataChangeMessageEntity> myShardDatas = Lists.newArrayList();
        for (DataChangeMessageEntity dataChangeMessageEntity : allShardDatas) {

            //保证同一个房间的消息由同一个分片处理
            int shardingItem = dataChangeMessageEntity.getChangeKey().hashCode() % context.getShardingTotalCount();
            if (shardingItems.contains(shardingItem)) {
                myShardDatas.add(dataChangeMessageEntity);
            }

        }
        if (CollectionUtils.isEmpty(myShardDatas)) {
            logSB.append("|分片后没有待发送的消息，放弃执行");
            log.warn(logSB.toString());
            return;
        }
        logSB.append("|分片后待发送的消息数量:" + myShardDatas.size()).append(SymbolConstant.BAR);

        //交给实际任务类执行
        for (DataChangeMessageEntity myShardData : myShardDatas) {
            processMyShardData(myShardData);
        }

        logSB.append("|结束");
        logSB.append("|耗时:" + (System.currentTimeMillis() - start));
        log.info(logSB.toString());
    }

    /**
     * 从数据库中得到所有分片的待发送消息
     */
    private List<DataChangeMessageEntity> getAllShardDatas() {
        if (dataChangeMessageDao == null){
            dataChangeMessageDao = (DataChangeMessageDao)ContextLoaderListener.getCurrentWebApplicationContext().getBean("dataChangeMessageDao");
        }
        List<DataChangeMessageEntity> dataChangeMessageEntities = dataChangeMessageDao.findList(
                Criteria.where("msgStatus", MsgStatusEnum.MSG_UN_SEND.getCode()));
        return dataChangeMessageEntities;
    }

    /**
     * 分片后本节点处理单个待发送消息
     */
    private void processMyShardData(DataChangeMessageEntity myShardData) {
        if (dataChangeMessageSendExecutor == null){
            this.dataChangeMessageSendExecutor = (DataChangeMessageSendExecutor)ContextLoaderListener.getCurrentWebApplicationContext().getBean("dataChangeMessageSendExecutor");
        }
        dataChangeMessageSendExecutor.execute(myShardData);

    }
}
