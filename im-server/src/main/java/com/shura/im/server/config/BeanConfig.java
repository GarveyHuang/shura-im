package com.shura.im.server.config;

import com.shura.im.common.constant.Constants;
import com.shura.im.common.protocol.IMReqMsg;
import okhttp3.OkHttpClient;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: Bean 注册配置类
 */
@Configuration
public class BeanConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public ZkClient buildZkClient() {
        return new ZkClient(appConfig.getZkAddr(), appConfig.getZkConnectTimeout());
    }

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
     * 创建心跳单例
     */
    @Bean(value = "heartBeat")
    public IMReqMsg heartBeat() {
        return new IMReqMsg(0L, "pong", Constants.CommandType.PING);
    }
}
