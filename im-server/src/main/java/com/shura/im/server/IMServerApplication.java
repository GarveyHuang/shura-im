package com.shura.im.server;

import com.shura.im.server.config.AppConfig;
import com.shura.im.server.kit.RegistryZookeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 应用启动入口
 */
@SpringBootApplication
public class IMServerApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMServerApplication.class);

    @Autowired
    private AppConfig appConfig;

    @Value("server.port")
    private int httpPort;

    public static void main(String[] args) {
        SpringApplication.run(IMServerApplication.class, args);
        LOGGER.info("Start im server success!!!");
    }

    @Override
    public void run(String... args) throws Exception {
        // 获得本机 IP
        String addr = InetAddress.getLocalHost().getHostAddress();
        Thread thread = new Thread(new RegistryZookeeper(addr, appConfig.getImServerPort(), httpPort));
        thread.setName("registry-zk");
        thread.start();
    }
}
