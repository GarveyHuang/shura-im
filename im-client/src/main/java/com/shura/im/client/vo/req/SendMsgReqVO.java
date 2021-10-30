package com.shura.im.client.vo.req;

import com.shura.im.common.req.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
public class SendMsgReqVO extends BaseRequest {

    @NotBlank(message = "msg 不能为空")
    private String msg;

    @NotNull(message = "userId 不能为空")
    private Long userId;

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
}
