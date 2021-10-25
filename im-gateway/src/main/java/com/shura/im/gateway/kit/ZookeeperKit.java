package com.shura.im.gateway.kit;

import com.alibaba.fastjson.JSON;
import com.shura.im.gateway.cache.ServerCache;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: Zookeeper 工具
 */
@Component
public class ZookeeperKit {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperKit.class);

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ServerCache serverCache;

    /**
     * 监听事件
     */
    public void subscribeEvent(String path) {
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
                LOGGER.info("Clear and update local cache parentPath=[{}], currentChildren=[{}]", parentPath, currentChildren.toString());

                serverCache.updateCache(currentChildren);
            }
        });
    }

    public List<String> getAllNode() {
        List<String> children = zkClient.getChildren("/route");
        LOGGER.info("Query all node = [{}].", JSON.toJSONString(children));
        return children;
    }
}
