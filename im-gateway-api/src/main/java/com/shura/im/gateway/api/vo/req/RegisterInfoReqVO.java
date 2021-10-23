package com.shura.im.gateway.api.vo.req;

import com.shura.im.common.req.BaseRequest;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 注册请求 VO
 */
public class RegisterInfoReqVO extends BaseRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RegisterInfoReqVO{" +
                "username='" + username + '\'' +
                "} " + super.toString();
    }
}
