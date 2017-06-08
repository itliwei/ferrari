package com.ziroom.ferrari.executor;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import com.ziroom.ferrari.task.DataChangeMessageQueue;
import com.ziroom.ferrari.task.DataChangeMessageQueueSendTask;
import com.ziroom.ferrari.task.DataChangeMessageQueueTask;
import com.ziroom.ferrari.task.MessageDataQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 放入队列线程池
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
@Component
public class DataChangeMessageExecutor {
    private Executor threadPoolExecutor;
    private MessageDataQueue messageDataQueue = new MessageDataQueue();
    private DataChangeMessageSendExecutor dataChangeMessageSendExecutor;


    public DataChangeMessageExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                messageDataQueue);
        //初始化另一个发送任务线程池
        dataChangeMessageSendExecutor = new DataChangeMessageSendExecutor();
    }

    public static void main(String[] args) throws InterruptedException {
        DataChangeMessageExecutor dataChangeMessageExecutor = new DataChangeMessageExecutor();
        //线程池发送MQ
        for(int i=1;i<100;i++) {
            int r = (int)Math.random()*100;
            DataChangeMessageEntity dataChangeMessageEntity = new DataChangeMessageEntity();
            dataChangeMessageEntity.setMsgFunction("AMS");
            dataChangeMessageEntity.setChangeKey(i+"");
            dataChangeMessageEntity.setMsgId(i+"");
            DataChangeMessageQueueTask dataChangeMessageQueueTask = new DataChangeMessageQueueTask(
                    dataChangeMessageExecutor.dataChangeMessageSendExecutor,
                    dataChangeMessageExecutor.messageDataQueue,dataChangeMessageEntity);

            dataChangeMessageExecutor.threadPoolExecutor.execute(dataChangeMessageQueueTask);
        }

    }

}
