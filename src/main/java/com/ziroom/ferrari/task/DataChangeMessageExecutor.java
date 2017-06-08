package com.ziroom.ferrari.task;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.domain.DataChangeMessageEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by homelink on 2017/6/8 0008.
 */
@Slf4j
@Getter
public class DataChangeMessageExecutor {
    private DataChangeMessageQueue dataChangeMessageQueue = new DataChangeMessageQueue();

    public DataChangeMessageExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new MessageDataQueue());
    }
}
