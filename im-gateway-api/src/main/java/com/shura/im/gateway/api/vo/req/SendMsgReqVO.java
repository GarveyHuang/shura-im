package com.shura.im.gateway.api.vo.req;

import com.shura.im.common.req.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 发送消息请求 VO
 */
public class SendMsgReqVO extends BaseRequest {

    @NotBlank(message = "msg 不能为空")
    private String msg;

    @NotNull(message = "id 不能为空")
    private long id;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
