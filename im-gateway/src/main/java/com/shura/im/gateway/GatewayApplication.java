package com.shura.im.gateway;

import com.shura.im.gateway.kit.ServerListListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        LOGGER.info("Start im route success!!!");
    }

    @Override
    public void run(String... args) throws Exception {
        // 监听服务
        Thread thread = new Thread(new ServerListListener());
        thread.setName("zk-listener");
        thread.start();
    }
}
