package com.shura.im.client.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.shura.im.client.handle.MsgHandleCaller;
import com.shura.im.client.listener.impl.MsgCallBackListener;
import com.shura.im.common.constant.Constants;
import com.shura.im.common.data.contruct.RingBufferWheel;
import com.shura.im.common.protocol.IMReqMsg;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Author: Garvey
 * @Created: 2021/11/17
 * @Description: Bean 注册配置
 */
@Configuration
public class BeanConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConfig.class);

    @Value("${im.user.id}")
    private long userId;

    @Value("${im.callback.thread.queue.size}")
    private int queueSize;

    @Value("${im.callback.thread.pool.size}")
    private int poolSize;

    /**
     * 创建心跳单例
     */
    @Bean(value = "heartBeat")
    public IMReqMsg heartBeat() {
        return new IMReqMsg(userId, "ping", Constants.CommandType.PING);
    }

    /**
     * http client
     */
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }

    /**
     * 创建回调线程池
     */
    @Bean("callbackThreadPool")
    public ThreadPoolExecutor buildCallerThreadPool() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(queueSize);
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setNameFormat("msg-callback-%d")
                .setDaemon(true)
                .build();
        return new ThreadPoolExecutor(poolSize, poolSize, 1, TimeUnit.MILLISECONDS, queue, factory);
    }

    @Bean("scheduledTask")
    public ScheduledExecutorService buildSchedule() {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setNameFormat("reConnect-job-%d")
                .setDaemon(true)
                .build();
        return new ScheduledThreadPoolExecutor(1, factory);
    }

    /**
     * 回调 bean
     */
    @Bean
    public MsgHandleCaller handleCaller() {
        return new MsgHandleCaller(new MsgCallBackListener());
    }

    @Bean
    public RingBufferWheel bufferWheel() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        return new RingBufferWheel(executorService);
    }
}
