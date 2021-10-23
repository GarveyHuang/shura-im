package com.shura.im.server.kit;

import com.shura.im.server.config.AppConfig;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: Zookeeper 工具
 */
@Component
public class ZookeeperKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperKit.class);

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private AppConfig appConfig;

    /**
     * 创建父级节点
     */
    public void createRouteNode() {
        boolean exists = zkClient.exists(appConfig.getZkRoot());
        if (exists) {
            return;
        }

        // 创建 root
        zkClient.createPersistent(appConfig.getZkRoot());
    }

    /**
     * 写入指定节点到临时目录
     */
    public void createNode(String path) {
        zkClient.createEphemeral(path);
    }
}
