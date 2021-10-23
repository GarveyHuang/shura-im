package com.shura.im.gateway.api.vo.req;

import com.shura.im.common.req.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 群聊请求 VO
 */
public class GroupReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    private Long userId;


    @NotBlank(message = "msg 不能为空")
    private String msg;

    public GroupReqVO() {
    }

    public GroupReqVO(Long userId, String msg) {
        this.userId = userId;
        this.msg = msg;
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
        return "GroupReqVO{" +
                "userId=" + userId +
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
