package com.ziroom.ferrari.config;

import com.google.common.collect.Lists;
import com.ziroom.rent.common.orm.DialectEnum;
import com.ziroom.rent.common.orm.dao.jdbc.JdbcSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Author zhoutao
 * @Date 2017/6/9
 */
@Slf4j
@Configuration
public class DaoSettingBeanConfig {
    //ps:幸好各系统主库驱动类名配置变量名称一致
    @Value("${dataSource.master.driverClassName}")
    private String masterDriverClassName;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired(required = false)
    @Qualifier(value = "dataSource")
    private DataSource zcMasterDataSource;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired(required = false)
    @Qualifier(value = "masterDataSource")
    private DataSource invMasterDataSource;

    @Bean(name = "dataChangeMessageDaoSettingBean")
    public JdbcSettings dataChangeMessageDaoSettingBean() {
        JdbcSettings jdbcSettings = new JdbcSettings();
        //资产主库是oracle
        if (masterDriverClassName.contains("oracle")) {
            jdbcSettings.setDialectEnum(DialectEnum.ORACLE);
            jdbcSettings.setWriteDataSource(Lists.newArrayList(zcMasterDataSource));
        }
        //库存主库是mysql
        else if (masterDriverClassName.contains("mysql")) {
            jdbcSettings.setDialectEnum(DialectEnum.MYSQL);
            jdbcSettings.setWriteDataSource(Lists.newArrayList(invMasterDataSource));
        }

        return jdbcSettings;
    }
}
