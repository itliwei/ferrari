package com.ziroom.ferrari;

import com.google.common.collect.Maps;
import com.ziroom.ferrari.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ziroom.ferrari.constants.Constants.DEV_KEY;

/**
 * @author dongh38@ziroom.com
 * @version 1.0.0
 */
@SpringBootApplication
@Slf4j
public class FerrariApplication {

    @Resource
    private Environment environment;

    @PostConstruct
    public void initApplication() {
        log.info("Running with Spring profile:{}", Arrays.toString(environment.getActiveProfiles()));
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.contains(DEV_KEY) && activeProfiles.contains(Constants.PROD_KEY)) {
            log.error("You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }

    }

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(FerrariApplication.class);
         addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://127.0.0.1:{}\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));


    }

    private static void addDefaultProfile(SpringApplication app) {
        Map<String,Object> defaultProperties = Maps.newHashMap();
        defaultProperties.put("spring.profiles.default",DEV_KEY);
        app.setDefaultProperties(defaultProperties);
    }

}
