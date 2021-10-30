package com.shura.im.client.service;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
public interface EchoService {

    /**
     * 输出消息到终端
     * @param msg message
     * @param replace
     */
    void echo(String msg, Object... replace) ;
}
