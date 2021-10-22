package com.shura.im.common.kit;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
public interface HeartBeatHandler {

    /**
     * 处理心跳
     * @param ctx
     * @throws Exception
     */
    void process(ChannelHandlerContext ctx) throws Exception;
}
