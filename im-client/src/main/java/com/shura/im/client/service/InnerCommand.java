package com.shura.im.client.service;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
public interface InnerCommand {

    /**
     * 执行
     * @param msg
     */
    void process(String msg) ;
}
