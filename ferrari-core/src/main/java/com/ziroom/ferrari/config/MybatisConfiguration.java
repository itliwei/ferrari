package com.ziroom.ferrari.config;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author dongh38@ziroom
 * @Date 16/11/24
 * @Time 下午3:49
 */
@org.springframework.context.annotation.Configuration
@MapperScan("com.ziroom.ferrari.dao")
public class MybatisConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfiguration(configuration());
        sqlSessionFactory.setTypeAliasesPackage("com.ziroom.ferrari.domain");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
        return sqlSessionFactory.getObject();
    }


    @Bean
    public Configuration configuration() {
        Configuration configuration = new Configuration();
        configuration.setCacheEnabled(true);
        configuration.setUseGeneratedKeys(true);
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        configuration.setLazyLoadingEnabled(true);
        configuration.setDefaultStatementTimeout(5000);
        configuration.setMapUnderscoreToCamelCase(true);
        return configuration;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
