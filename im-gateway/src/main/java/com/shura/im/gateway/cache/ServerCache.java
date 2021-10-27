package com.shura.im.gateway.cache;

import com.google.common.cache.LoadingCache;
import com.shura.im.gateway.kit.ZookeeperKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 服务器节点缓存
 */
@Component
public class ServerCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerCache.class);

    @Autowired
    private LoadingCache<String, String> cache;

    @Autowired
    private ZookeeperKit zookeeperKit;

    public void addCache(String key) {
        cache.put(key, key);
    }

    /**
     * 更新所有缓存
     *
     * 先删除，再新增
     */
    public void updateCache(List<String> currentChildren) {
        cache.invalidateAll();

        for (String currentChild : currentChildren) {
            String key;
            if (currentChild.split("-").length == 2) {
                key = currentChild.split("-")[1];
            } else {
                key = currentChild;
            }
            addCache(key);
        }
    }

    /**
     * 获取所有的服务列表
     */
    public List<String> getServerList() {
        List<String> list = new ArrayList<>();

        if (cache.size() == 0) {
            List<String> allNode = zookeeperKit.getAllNode();
            for (String node : allNode) {
                String key = node.split("-")[1];
                addCache(key);
            }
        }

        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            list.add(entry.getKey());
        }

        return list;
    }

    public void rebuildCacheList() {
        updateCache(getServerList());
    }
}
