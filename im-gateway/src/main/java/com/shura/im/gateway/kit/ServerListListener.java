package com.shura.im.gateway.kit;

import com.shura.im.gateway.config.AppConfig;
import com.shura.im.gateway.util.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
public class ServerListListener implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListListener.class);

    private ZookeeperKit zookeeperKit;

    private AppConfig appConfig;

    public ServerListListener() {
        zookeeperKit = SpringBeanFactory.getBean(ZookeeperKit.class);
        appConfig = SpringBeanFactory.getBean(AppConfig.class);
    }

    @Override
    public void run() {
        // 注册监听服务
        zookeeperKit.subscribeEvent(appConfig.getZkRoot());

    }
}
