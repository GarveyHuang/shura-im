package com.shura.im.common.pojo;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 用户信息
 */
public class IMUserInfo {

    private Long userId;

    private String username;

    public IMUserInfo(Long userId, String username) {
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

    public void setUsername(String userName) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "IMUserInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
