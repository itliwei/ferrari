package com.ziroom.ferrari.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by homelink on 2017/6/5 0005.
 */
public class MyThreadPool {
    /**
     * 自定义线程池
     */
    public ExecutorService executorService;

    /**
     * 1.在使用有界队列的时候：若有新的任务需要执行，如果线程池实际线程数小于corePoolSize核心线程数的时候，则优先创建线程。
     * 若大于corePoolSize时，则会将多余的线程存放在队列中，
     * 若队列已满，且最请求线程小于maximumPoolSize的情况下，则自定义的线程池会创建新的线程，
     * 若队列已满，且最请求线程大于maximumPoolSize的情况下，则执行拒绝策略，或其他自定义方式。
     * ArrayBlockingQueue(有界队列)
     * LinkedBlockingQueue(无界队列)
     */
    public MyThreadPool() {
        this.executorService = new ThreadPoolExecutor(
                2, // coreSize
                8, // maxSize
                1, // 60s
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(16) // 无界队列，等待执行
        );
    }

    public static void main(String[] args) throws InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool();
        //线程池发送MQ
        for (int i=0;i<1000;i++) {
            CustomTask mqTask = new CustomTask(i);
            myThreadPool.executorService.execute(mqTask);
        }
        myThreadPool.executorService.shutdown();
    }


}

class CustomTask implements Runnable{
    private  int id;
    public CustomTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("#" + id + "   threadId=" + Thread.currentThread().getName() );
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}