package com.shura.im.client.thread;

import com.shura.im.client.service.impl.ClientHeartBeatHandlerImpl;
import com.shura.im.client.util.SpringBeanFactory;
import com.shura.im.common.kit.HeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Garvey
 * @Created: 2021/11/17
 * @Description:
 */
public class ReconnectJob implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectJob.class);

    private ChannelHandlerContext context;

    private HeartBeatHandler heartBeatHandler;

    public ReconnectJob(ChannelHandlerContext context) {
        this.context = context;
        this.heartBeatHandler = SpringBeanFactory.getBean(ClientHeartBeatHandlerImpl.class);
    }

    @Override
    public void run() {
        try {
            heartBeatHandler.process(context);
        } catch (Exception e) {
            LOGGER.error("exception", e);
        }
    }
}
