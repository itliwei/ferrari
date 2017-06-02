package com.ziroom.ferrari.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程任务config
 */
@Configuration
@EnableScheduling
public class TaskConfig implements SchedulingConfigurer {
    /**
     * 线程池
     * @return
     */
    @Bean(name = "commonExecutorService", destroyMethod = "shutdown")
    public ExecutorService commonExecutorService() {
        return Executors.newScheduledThreadPool(4);
    }

    /**
     * 定时任务
     * @param taskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(commonExecutorService());
    }
}
