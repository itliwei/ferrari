package com.ziroom.ferrari.task;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义拒绝策略
 * Created by liwei on 2017/6/9 0009.
 */
public class DataMessageRejectedPolicy implements RejectedExecutionHandler{

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // TODO 做点什么
    }
}
