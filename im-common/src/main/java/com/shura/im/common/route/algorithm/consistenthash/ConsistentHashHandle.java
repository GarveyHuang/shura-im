package com.shura.im.common.route.algorithm.consistenthash;

import com.shura.im.common.route.algorithm.RouteHandle;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description: 轮询策略，一致性哈希
 */
public class ConsistentHashHandle implements RouteHandle {

    private AbstractConsistentHash hash;

    public void setHash(AbstractConsistentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values, key);
    }
}
