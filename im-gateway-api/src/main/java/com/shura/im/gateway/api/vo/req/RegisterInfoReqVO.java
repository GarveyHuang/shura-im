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
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "RegisterInfoReqVO{" +
                "userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
