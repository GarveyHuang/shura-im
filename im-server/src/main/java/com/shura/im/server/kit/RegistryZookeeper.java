package com.shura.im.server.kit;

import com.shura.im.server.config.AppConfig;
import com.shura.im.server.util.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 注册 Zookeeper 线程
 */
public class RegistryZookeeper implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryZookeeper.class);

    private final ZookeeperKit zkKit;

    private final AppConfig appConfig;

    private final String ip;

    private final int imServerPort;

    private final int httpPort;

    public RegistryZookeeper(String ip, int imServerPort, int httpPort) {
        this.ip = ip;
        this.imServerPort = imServerPort;
        this.httpPort = httpPort;
        zkKit = SpringBeanFactory.getBean(ZookeeperKit.class);
        appConfig = SpringBeanFactory.getBean(AppConfig.class);
    }

    @Override
    public void run() {
        // 创建父节点
        zkKit.createRouteNode();

        // 是否将自己注册到 ZK
        if (appConfig.isZkSwitch()) {
            String path = appConfig.getZkRoot() + "/ip-" + ip + ":" + imServerPort + ":" + httpPort;
            zkKit.createNode(path);
            LOGGER.info("Registry zookeeper success, msg=[{}]", path);
        }
    }
}
