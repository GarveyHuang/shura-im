package com.shura.im.server.server;

import com.shura.im.common.constant.Constants;
import com.shura.im.common.protocol.IMReqMsg;
import com.shura.im.server.api.vo.req.SendMsgReqVO;
import com.shura.im.server.init.IMServerInitializer;
import com.shura.im.server.util.SessionSocketHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@Component
public class IMServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMServer.class);

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();

    @Value("${tim.server.port}")
    private int nettyPort;

    /**
     * 启动 im server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(nettyPort))
                // 保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new IMServerInitializer());

        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            LOGGER.info("Start im server success!!!");
        }
    }

    /**
     * 销毁 im server
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        LOGGER.info("Close im server success!!!");
    }

    /**
     * 发送消息给客户端
     */
    public void sendMsg(SendMsgReqVO sendMsgReqVO) {
        NioSocketChannel socketChannel = SessionSocketHolder.get(sendMsgReqVO.getUserId());

        if (null == socketChannel) {
            LOGGER.error("client {} offline!", sendMsgReqVO.getUserId());
            return;
        }

        IMReqMsg protocol = new IMReqMsg(sendMsgReqVO.getUserId(), sendMsgReqVO.getMsg(), Constants.CommandType.MSG);

        ChannelFuture future = socketChannel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture -> {
           LOGGER.info("Server push msg:[{}]", sendMsgReqVO.toString());
        });
    }
}
