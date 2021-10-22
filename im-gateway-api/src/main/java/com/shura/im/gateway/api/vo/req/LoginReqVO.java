package com.shura.im.gateway.api.vo.req;

import com.shura.im.common.req.BaseRequest;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 登录请求 VO
 */
public class LoginReqVO extends BaseRequest {

    private Long userId;
    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LoginReqVO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
