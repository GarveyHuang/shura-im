package com.shura.im.gateway.api.vo.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 私聊请求
 */
public class Person2PersonReqVO {

    @NotNull(message = "userId 不能为空")
    // 消息发送者的 userId
    private Long userId;


    @NotNull(message = "userId 不能为空")
    // 消息接收者的 userId
    private Long receiveUserId;


    @NotBlank(message = "msg 不能为空")
    private String msg;

    public Person2PersonReqVO() {
    }

    public Person2PersonReqVO(Long userId, Long receiveUserId, String msg) {
        this.userId = userId;
        this.receiveUserId = receiveUserId;
        this.msg = msg;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Person2PersonReqVO{" +
                "userId=" + userId +
                ", receiveUserId=" + receiveUserId +
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
