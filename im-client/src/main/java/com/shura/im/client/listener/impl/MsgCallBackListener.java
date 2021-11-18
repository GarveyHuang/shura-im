package com.shura.im.client.listener.impl;

import com.shura.im.client.listener.CustomMsgHandleListener;
import com.shura.im.client.service.MsgLoggerService;
import com.shura.im.client.util.SpringBeanFactory;

/**
 * @Author: Garvey
 * @Created: 2021/11/18
 * @Description: 接收消息自定义回调接口
 */
public class MsgCallBackListener implements CustomMsgHandleListener {

    private MsgLoggerService msgLoggerService ;

    public MsgCallBackListener() {
        this.msgLoggerService = SpringBeanFactory.getBean(MsgLoggerService.class) ;
    }

    @Override
    public void handle(String msg) {
        msgLoggerService.log(msg) ;
    }
}
