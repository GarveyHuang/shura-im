package com.shura.im.gateway.api.vo.res;

import com.shura.im.common.pojo.RouteInfo;

import java.io.Serializable;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: IM Server 信息 VO
 */
public class IMServerResVO implements Serializable {

    private String ip;
    private Integer imServerPort;
    private Integer httpPort;

    public IMServerResVO(RouteInfo routeInfo) {
        this.ip = routeInfo.getIp();
        this.imServerPort = routeInfo.getImServerPort();
        this.httpPort = routeInfo.getHttpPort();
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
