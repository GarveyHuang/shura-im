package com.shura.im.common.route.algorithm;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
public interface RouteHandle {

    /**
     * 在一批服务器里进行路由
     * @param values
     * @param key
     * @return
     */
    String routeServer(List<String> values, String key) ;
}
