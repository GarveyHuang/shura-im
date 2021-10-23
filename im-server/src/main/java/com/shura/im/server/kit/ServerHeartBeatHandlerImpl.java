package com.shura.im.server.kit;

import com.shura.im.common.kit.HeartBeatHandler;
import com.shura.im.common.pojo.IMUserInfo;
import com.shura.im.common.util.NettyAttrUtil;
import com.shura.im.server.config.AppConfig;
import com.shura.im.server.util.SessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@Service
public class ServerHeartBeatHandlerImpl implements HeartBeatHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerHeartBeatHandlerImpl.class);

    @Autowired
    private RouteHandler routeHandler;

    @Autowired
    private AppConfig config;

    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        long heartBeatTime = config.getHeartBeatTime() * 1000;

        Long lastReadTime = NettyAttrUtil.getReaderTime(ctx.channel());
        long now = System.currentTimeMillis();
        if (lastReadTime != null && now - lastReadTime > heartBeatTime) {
            IMUserInfo userInfo = SessionSocketHolder.getUserId((NioSocketChannel) ctx.channel());
            if (userInfo != null) {
                LOGGER.warn("客户端[{}]心跳超时[{}] ms，需要关闭连接!", userInfo.getUsername(), now - lastReadTime);
            }

            routeHandler.userOffLine(userInfo, (NioSocketChannel) ctx.channel());
            ctx.channel().close();
        }
    }
}
