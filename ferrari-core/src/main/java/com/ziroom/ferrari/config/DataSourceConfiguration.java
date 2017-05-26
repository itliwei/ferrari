package com.ziroom.ferrari.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ziroom.platform.tesla.druid.filter.CatStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.Collections;

import static com.ziroom.ferrari.constants.Constants.DEV_KEY;
import static com.ziroom.ferrari.constants.Constants.PROD_KEY;
import static com.ziroom.ferrari.constants.Constants.TEST_KEY;

/**
 * @author dongh38@ziroom
 * @Date 16/11/24
 * @Time 下午4:00
 */
@Configuration
public class DataSourceConfiguration {


    @Bean
    public CatStatFilter catStatFilter() {
        return new CatStatFilter();
    }


    @Bean(initMethod = "init",destroyMethod = "close")
    @Profile({DEV_KEY,PROD_KEY,TEST_KEY})
    public DataSource druidDataSource(@Value("${jdbc.driverClassName}")String driver,
                                      @Value("${jdbc.url}")String url,
                                      @Value("${jdbc.username}")String username,
                                      @Value("${jdbc.password}")String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setProxyFilters(Collections.singletonList(catStatFilter()));
        return dataSource;
    }




}
