package com.shura.im.server.api.vo.req;

import com.shura.im.common.req.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 发送消息请求参数 VO
 */
public class SendMsgReqVO extends BaseRequest {

    @NotBlank(message = "msg 不能为空")
    private String msg;

    @NotNull(message = "userId 不能为空")
    private Long userId;

    public SendMsgReqVO() { }

    public SendMsgReqVO(String msg, Long userId) {
        this.msg = msg;
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SendMsgReqVO{" +
                "msg='" + msg + '\'' +
                ", userId=" + userId +
                "} " + super.toString();
    }
}
