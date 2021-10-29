package com.shura.im.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
@SpringBootApplication
public class IMClientApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMClientApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(IMClientApplication.class, args);
        LOGGER.info("Start client success!!!");
    }
}
