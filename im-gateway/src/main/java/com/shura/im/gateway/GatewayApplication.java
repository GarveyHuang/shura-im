package com.shura.im.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@SpringBootApplication
public class GatewayApplication  {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        LOGGER.info("Start im route success!!!");
    }
}
