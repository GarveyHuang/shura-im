package com.shura.im.common.protocol;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
public class IMReqMsg {

    private Long requestId;
    private String reqMsg;
    private Integer type;

    public IMReqMsg() {
    }

    public IMReqMsg(Long requestId, String reqMsg, Integer type) {
        this.requestId = requestId;
        this.reqMsg = reqMsg;
        this.type = type;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getReqMsg() {
        return reqMsg;
    }

    public void setReqMsg(String reqMsg) {
        this.reqMsg = reqMsg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "IMReqMsg{" +
                "requestId=" + requestId +
                ", reqMsg='" + reqMsg + '\'' +
                ", type=" + type +
                '}';
    }
}
