package com.ziroom.ferrari.factory;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.ziroom.gaea.mq.rabbitmq.factory.RabbitConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**\
 * 自定义 RabbitFactory
 * Created by homelink on 2017/6/13 0013.
 */
public class RabbitFactory extends RabbitConnectionFactory {
    private String rabbitServer;
    private Integer rabbitServerPort;
    private String rabbitServerUsername;
    private String rabbitServerPassword;
    private String rabbitServerEnv;

    private static final Logger logger = LoggerFactory.getLogger(RabbitFactory.class);
    private ConnectionFactory connectionFactory = null;
    private Connection connection;
    private Address[] addresses;

    public RabbitFactory() {
        this.init();
    }

    public RabbitFactory(String rabbitServer,Integer rabbitServerPort,String rabbitServerUsername,
                         String rabbitServerPassword,String rabbitServerEnv) {
        this.rabbitServer = rabbitServer;
        this.rabbitServerPort = rabbitServerPort;
        this.rabbitServerUsername = rabbitServerUsername;
        this.rabbitServerPassword = rabbitServerPassword;
        this.rabbitServerEnv =rabbitServerEnv;
    }

    public ConnectionFactory getConnectFactory() {
        if(this.connectionFactory != null) {
            return this.connectionFactory;
        } else {
            if(this.initCheck()) {
                this.connectionFactory = new ConnectionFactory();
                this.connectionFactory.setHost(this.addresses[0].getHost());
                this.connectionFactory.setPort(this.rabbitServerPort);
                this.connectionFactory.setUsername(this.rabbitServerUsername);
                this.connectionFactory.setPassword(this.rabbitServerPassword);
                this.connectionFactory.setAutomaticRecoveryEnabled(true);
            }

            return this.connectionFactory;
        }
    }

    private boolean initCheck() {
        if(this.rabbitServer == null) {
            logger.error("rabbit.server配置不能为null !!!!");
            return false;
        } else if(this.rabbitServerPort == null ||this.rabbitServerPort == 0 ) {
            logger.error("rabbit.server.port配置不能为0 !!!!");
            return false;
        } else {
            this.initAddresses(this.rabbitServer, this.rabbitServerPort);
            if(this.rabbitServerUsername == null) {
                logger.error("rabbit.server.username配置不能为null !!!!");
                return false;
            } else if(this.rabbitServerPassword == null) {
                logger.error("rabbit.server.password配置不能为null !!!!");
                return false;
            } else {
                return true;
            }
        }
    }

    private void initAddresses(String hosts, int port) {
        String[] servers = hosts.split(",");
        this.addresses = new Address[servers.length];

        for(int i = 0; i < servers.length; ++i) {
            this.addresses[i] = new Address(servers[i], port);
        }

    }

    public String getEnvironment() {
        return this.rabbitServerEnv == null?null:this.rabbitServerEnv;
    }

    public Connection getConnection() throws Exception {
        if(this.connection == null || !this.connection.isOpen()) {
            synchronized(this) {
                if(this.connection == null || !this.connection.isOpen()) {
                    this.connection = this.getConnectFactory().newConnection(this.addresses);
                }
            }
        }

        return this.connection;
    }

    public Channel getChannel(Connection connection) throws IOException {
        return connection.createChannel();
    }

    public void closeChannel(Channel channel) throws IOException, TimeoutException {
        if(channel != null) {
            channel.close();
        }

    }
}
