package com.ziroom.ferrari.config;

import com.ziroom.ferrari.aop.LoggingAspect;
import com.ziroom.ferrari.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

/**
 * @author dongh38@ziroom
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    @Profile(Constants.DEV_KEY)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

}
