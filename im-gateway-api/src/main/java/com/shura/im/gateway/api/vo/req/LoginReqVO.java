package com.shura.im.gateway.api.vo.req;

import com.shura.im.common.req.BaseRequest;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 登录请求 VO
 */
public class LoginReqVO extends BaseRequest {

    private Long userId;
    private String username;

    public LoginReqVO() {}

    public LoginReqVO(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginReqVO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                "} " + super.toString();
    }
}
