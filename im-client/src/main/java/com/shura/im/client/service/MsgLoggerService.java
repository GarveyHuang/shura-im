package com.shura.im.client.service;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
public interface MsgLoggerService {

    /**
     * 异步写入消息
     * @param msg
     */
    void log(String msg) ;


    /**
     * 停止写入
     */
    void stop() ;

    /**
     * 查询聊天记录
     * @param key 关键字
     * @return
     */
    String query(String key) ;
}
