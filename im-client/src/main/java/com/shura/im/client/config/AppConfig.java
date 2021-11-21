package com.shura.im.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: Garvey
 * @Created: 2021/11/21
 * @Description:
 */
@Component
public class AppConfig {

    @Value("${im.user.id}")
    private Long userId;

    @Value("${im.user.username}")
    private String username;

    @Value("${im.msg.logger.path}")
    private String msgLoggerPath;

    @Value("${im.heartbeat.time}")
    private long heartBeatTime;

    @Value("5")
    private int errorCount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsgLoggerPath() {
        return msgLoggerPath;
    }

    public void setMsgLoggerPath(String msgLoggerPath) {
        this.msgLoggerPath = msgLoggerPath;
    }

    public long getHeartBeatTime() {
        return heartBeatTime;
    }

    public void setHeartBeatTime(long heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
}
