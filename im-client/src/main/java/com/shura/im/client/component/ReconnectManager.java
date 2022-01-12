package com.shura.im.client.component;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.shura.im.client.thread.ReconnectJob;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
@Component
public class ReconnectManager {

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Trigger reconnect job
     * @param ctx
     */
    public void reconnect(ChannelHandlerContext ctx) {
        buildExecutor() ;
        scheduledExecutorService.scheduleAtFixedRate(new ReconnectJob(ctx),0,10, TimeUnit.SECONDS) ;
    }

    /**
     * Close reconnect job if reconnect success.
     */
    public void reconnectSuccess(){
        scheduledExecutorService.shutdown();
    }


    /***
     * build a thread executor
     * @return
     */
    private ScheduledExecutorService buildExecutor() {
        if (scheduledExecutorService == null || scheduledExecutorService.isShutdown()) {
            ThreadFactory scheduledFactory = new ThreadFactoryBuilder()
                    .setNameFormat("reconnect-job-%d")
                    .setDaemon(true)
                    .build();
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1, scheduledFactory);
        }

        return scheduledExecutorService;
    }
}
