package com.shura.im.common.req;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 请求基本参数封装
 */
public class BaseRequest {

    /**
     * 唯一请求号
     */
    private String reqNo;

    /**
     * 当前请求的时间戳
     */
    private int timeStamp;

    public BaseRequest() {
        this.setTimeStamp((int) (System.currentTimeMillis() / 1000));
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public String toString() {
        return "BaseRequest{" +
                "reqNo='" + reqNo + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
