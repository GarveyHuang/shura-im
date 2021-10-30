package com.shura.im.client.vo.req;

import com.shura.im.common.req.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
public class GoogleProtocolVO extends BaseRequest {

    @NotNull(message = "requestId 不能为空")
    private Long requestId;

    @NotBlank(message = "msg 不能为空")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "GoogleProtocolVO{" +
                "requestId=" + requestId +
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
