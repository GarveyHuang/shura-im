package com.shura.im.gateway.api.vo.res;

import java.io.Serializable;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 发送消息回复 VO
 */
public class SendMsgResVO implements Serializable {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
