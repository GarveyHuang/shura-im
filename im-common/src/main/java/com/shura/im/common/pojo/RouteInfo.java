package com.shura.im.common.pojo;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 路由信息
 */
public class RouteInfo {

    private String ip;
    private Integer imServerPort;
    private Integer httpPort;

    public RouteInfo(String ip, Integer imServerPort, Integer httpPort) {
        this.ip = ip;
        this.imServerPort = imServerPort;
        this.httpPort = httpPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getImServerPort() {
        return imServerPort;
    }

    public void setImServerPort(Integer imServerPort) {
        this.imServerPort = imServerPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
}
