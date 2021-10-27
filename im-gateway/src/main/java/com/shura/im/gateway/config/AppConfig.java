package com.shura.im.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@Component
public class AppConfig {

    @Value("${app.zk.root}")
    private String zkRoot;

    @Value("${app.zk.addr}")
    private String zkAddr;


    @Value("${server.port}")
    private int port;

    @Value("${app.zk.connect.timeout}")
    private int zkConnectTimeout;

    @Value("${app.route.way}")
    private String routeWay;

    @Value("${app.route.way.consitenthash:com.shura.im.common.route.algorithm.consistenthash.TreeMapConsistentHash}")
    private String consistentHashWay;

    public int getZkConnectTimeout() {
        return zkConnectTimeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getZkRoot() {
        return zkRoot;
    }

    public void setZkRoot(String zkRoot) {
        this.zkRoot = zkRoot;
    }

    public String getZkAddr() {
        return zkAddr;
    }

    public void setZkAddr(String zkAddr) {
        this.zkAddr = zkAddr;
    }

    public String getRouteWay() {
        return routeWay;
    }

    public void setRouteWay(String routeWay) {
        this.routeWay = routeWay;
    }

    public String getConsistentHashWay() {
        return consistentHashWay;
    }

    public void setConsistentHashWay(String consistentHashWay) {
        this.consistentHashWay = consistentHashWay;
    }
}
