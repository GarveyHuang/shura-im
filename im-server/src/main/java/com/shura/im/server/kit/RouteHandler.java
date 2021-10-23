package com.shura.im.server.kit;

import com.shura.im.common.pojo.IMUserInfo;
import com.shura.im.server.config.AppConfig;
import com.shura.im.server.util.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@Component
public class RouteHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteHandler.class);

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private AppConfig config;

    /**
     * 用户下线
     *
     * @param userInfo
     * @param channel
     * @throws IOException
     */
    public void userOffLine(IMUserInfo userInfo, NioSocketChannel channel) throws IOException {
        if (userInfo != null) {
            LOGGER.info("Account [{}] offline", userInfo.getUsername());
            SessionSocketHolder.removeSession(userInfo.getUserId());
            // 清除路由关系
            clearRouteInfo(userInfo);
        }
        SessionSocketHolder.remove(channel);

    }

    /**
     * 清除路由关系
     * @param userInfo
     */
    public void clearRouteInfo(IMUserInfo userInfo) {
        // TODO 路由 api 尚未实现
    }
}
