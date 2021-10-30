package com.shura.im.client.service.impl;

import com.shura.im.client.client.IMClient;
import com.shura.im.client.thread.ContextHolder;
import com.shura.im.common.kit.HeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2021/10/26
 * @Description:
 */
@Service
public class ClientHeartBeatHandlerImpl implements HeartBeatHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHeartBeatHandlerImpl.class);

    @Autowired
    private IMClient imClient;

    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        // 重连
        ContextHolder.setReconnect(true);
        imClient.reconnect();
    }
}
