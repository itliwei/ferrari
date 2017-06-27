package com.ziroom.ferrari.config;

import com.dangdang.ddframe.job.api.JobScheduler;
import com.dangdang.ddframe.job.api.config.JobConfigurationFactory;
import com.dangdang.ddframe.job.api.config.impl.SimpleJobConfiguration;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.ziroom.ferrari.elasticjob.MsgSendRecoupJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化消息发送补偿作业
 *
 * @author zhoutao
 * @date 2017/6/9
 */
@Slf4j
@Configuration
public class MsgSendRecoupJobConfig {
    @Value("${job.regCenter.serverLists}")
    private String zkServerList;
    //    @Value("${msgSendRecoupJob.jobCron}")
    private String jobCron = "0 * * * * ?";
    //    @Value("${msgSendRecoupJob.shardingTotalCount}")
    private int shardingCount = 4; //数据分片，通常是各系统的理论机器各数

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired(required = false)
    @Qualifier(value = "regCenter")
    private CoordinatorRegistryCenter registryCenter;

    /**
     * 定义dataChangeMessageJobScheduler
     *
     * @return
     */
    @Bean(name = "dataChangeMessageJobScheduler")
    public JobScheduler dataChangeMessageJobScheduler() {
        if (registryCenter == null) {
            ZookeeperConfiguration ferrariZkConfig = new ZookeeperConfiguration(zkServerList, "ferrari-job", 1000, 3000, 3);
            registryCenter = new ZookeeperRegistryCenter(ferrariZkConfig);
            registryCenter.init();
        }

        SimpleJobConfiguration simpleJobConfig = JobConfigurationFactory
                .createSimpleJobConfigurationBuilder(MsgSendRecoupJob.JOB_NAME, MsgSendRecoupJob.class, shardingCount, jobCron)
                .monitorExecution(false)
                .misfire(false)
                .overwrite(true)
                .build();

        JobScheduler jobScheduler = new JobScheduler(registryCenter, simpleJobConfig);
        jobScheduler.init();

        log.info(MsgSendRecoupJob.JOB_DESC + "*************启动*********************");

        return jobScheduler;
    }
}
