package com.shura.im.client;

import com.shura.im.client.component.ClientInfo;
import com.shura.im.client.scanner.Scan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
@SpringBootApplication
public class IMClientApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMClientApplication.class);

    @Autowired
    private ClientInfo clientInfo;

    public static void main(String[] args) {
        SpringApplication.run(IMClientApplication.class, args);
        LOGGER.info("Start client success!!!");
    }

    @Override
    public void run(String... args) throws Exception {
        Scan scan = new Scan();
        Thread thread = new Thread(scan);
        thread.setName("scan-thread");
        thread.start();
        clientInfo.saveStartDate();
    }
}
