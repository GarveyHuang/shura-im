package com.shura.im.gateway.service;

import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.exception.IMException;
import com.shura.im.common.pojo.RouteInfo;
import com.shura.im.gateway.cache.ServerCache;
import com.shura.im.gateway.kit.NetAddressIsReachable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
@Component
public class CommonBizService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonBizService.class);

    @Autowired
    private ServerCache serverCache;

    /**
     * 检查 ip、端口
     *
     * @param routeInfo
     */
    public void checkServerAvailable(RouteInfo routeInfo) {
        boolean reachable = NetAddressIsReachable.checkAddressReachable(routeInfo.getIp(), routeInfo.getImServerPort(), 1000);
        if (!reachable) {
            LOGGER.error("ip={}, port={} are not available", routeInfo.getIp(), routeInfo.getImServerPort());

            // rebuild cache
            serverCache.rebuildCacheList();
            throw new IMException(StatusEnum.SERVER_NOT_AVAILABLE);
        }

    }
}
