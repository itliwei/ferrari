package com.ziroom.ferrari.task;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author zhoutao
 * @Date 2017/5/5
 */
@Slf4j
public class DataChangeMessageExecutor {
    public ThreadPoolExecutor dataChangeMessageExecutor;
    /**
     * 1.在使用有界队列的时候：若有新的任务需要执行，如果线程池实际线程数小于corePoolSize核心线程数的时候，则优先创建线程。
     * 若大于corePoolSize时，则会将多余的线程存放在队列中，
     * 若队列已满，且最请求线程小于maximumPoolSize的情况下，则自定义的线程池会创建新的线程，
     * 若队列已满，且最请求线程大于maximumPoolSize的情况下，则执行拒绝策略，或其他自定义方式。
     * ArrayBlockingQueue(有界队列)
     * LinkedBlockingQueue(无界队列)
     */
    public DataChangeMessageExecutor() {
        dataChangeMessageExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new MessageDataQueue());
    }

}
