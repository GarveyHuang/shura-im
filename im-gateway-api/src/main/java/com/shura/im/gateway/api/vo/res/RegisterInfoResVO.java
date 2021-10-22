package com.shura.im.gateway.api.vo.res;

import java.io.Serializable;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 注册信息返回 VO
 */
public class RegisterInfoResVO implements Serializable {

    private Long userId;
    private String username;

    public RegisterInfoResVO(Long userId, String username) {
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
        return "RegisterInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
