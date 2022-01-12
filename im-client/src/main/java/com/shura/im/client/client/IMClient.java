package com.shura.im.client.client;

import com.shura.im.client.component.ClientInfo;
import com.shura.im.client.component.ReconnectManager;
import com.shura.im.client.config.AppConfig;
import com.shura.im.client.init.IMClientHandleInitializer;
import com.shura.im.client.service.EchoService;
import com.shura.im.client.service.MsgHandle;
import com.shura.im.client.service.RouteRequestService;
import com.shura.im.client.thread.ContextHolder;
import com.shura.im.client.vo.req.GoogleProtocolVO;
import com.shura.im.client.vo.res.IMServerResVO;
import com.shura.im.common.constant.Constants;
import com.shura.im.common.protocol.IMReqMsg;
import com.shura.im.gateway.api.vo.req.LoginReqVO;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: Garvey
 * @Created: 2021/11/23
 * @Description:
 */
@Component
public class IMClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMClient.class);

    private final EventLoopGroup group = new NioEventLoopGroup(1, new DefaultThreadFactory("im-work"));

    @Value("${im.user.id}")
    private long userId;

    @Value("${im.user.username}")
    private String username;

    private SocketChannel channel;

    @Autowired
    private EchoService echoService;

    @Autowired
    private RouteRequestService routeRequestService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private MsgHandle msgHandle;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private ReconnectManager reconnectManager;

    /**
     * 重试次数
     */
    private int errorCount;

    @PostConstruct
    public void start() throws Exception {
        // 登录 + 获取可以使用的服务器 ip + port
        IMServerResVO.ServerInfo imServer = userLogin();

        //启动客户端
        startClient(imServer);

        //向服务端注册
        loginIMServer();
    }

    /**
     * 启动客户端
     *
     * @param imServer
     * @throws Exception
     */
    private void startClient(IMServerResVO.ServerInfo imServer) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new IMClientHandleInitializer());

        ChannelFuture future = null;
        try {
            future = bootstrap.connect(imServer.getIp(), imServer.getImServerPort()).sync();
        } catch (Exception e) {
            errorCount++;

            if (errorCount >= appConfig.getErrorCount()) {
                LOGGER.error("连接失败次数达到上限[{}]次", errorCount);
                msgHandle.shutdown();
                this.close();
            }
            LOGGER.error("Connect fail!", e);
            throw new RuntimeException("Connect server fail, start im client fail!");
        }

        if (future.isSuccess()) {
            channel = (SocketChannel) future.channel();

            echoService.echo("Start im client success!");
            LOGGER.info("启动 im client 成功");
        }
    }

    /**
     * 登录 + 路由服务器
     *
     * @return 路由服务器信息
     * @throws Exception
     */
    private IMServerResVO.ServerInfo userLogin() throws InterruptedException {
        LoginReqVO loginReqVO = new LoginReqVO(userId, username);
        IMServerResVO.ServerInfo imServer = null;
        try {
            imServer = routeRequestService.getIMServer(loginReqVO);

            //保存系统信息
            clientInfo.saveServiceInfo(imServer.getIp() + ":" + imServer.getImServerPort())
                    .saveUserInfo(userId, username);

            LOGGER.info("imServer=[{}]", imServer.toString());
        } catch (Exception e) {
            errorCount++;

            if (errorCount >= appConfig.getErrorCount()) {
                echoService.echo("The maximum number of reconnections has been reached [{}] times, close im client!", errorCount);
                msgHandle.shutdown();
                this.close();
            }
            LOGGER.error("login fail", e);
        }
        return imServer;
    }

    /**
     * 向服务器注册
     */
    private void loginIMServer() {
        IMReqMsg login = new IMReqMsg(userId, username, Constants.CommandType.LOGIN);
        ChannelFuture future = channel.writeAndFlush(login);
        future.addListener((ChannelFutureListener) channelFuture ->
                echoService.echo("Registry im server success!"));
    }

    /**
     * 发送消息字符串
     *
     * @param msg
     */
    public void sendStringMsg(String msg) {
        ByteBuf message = Unpooled.buffer(msg.getBytes().length);
        message.writeBytes(msg.getBytes());
        ChannelFuture future = channel.writeAndFlush(message);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("客户端手动发消息成功={}", msg));

    }

    /**
     * 发送 Google Protocol 编解码字符串
     *
     * @param googleProtocolVO
     */
    public void sendGoogleProtocolMsg(GoogleProtocolVO googleProtocolVO) {

        IMReqMsg protocol = new IMReqMsg(googleProtocolVO.getRequestId(), googleProtocolVO.getMsg(), Constants.CommandType.MSG);
        ChannelFuture future = channel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("客户端手动发送 Google Protocol 成功={}", googleProtocolVO));

    }


    /**
     * 1. 清除路由信息；
     * 2. 重连；
     * 3. 关闭重连任务；
     * 4. 重置重连状态。
     *
     * @throws Exception
     */
    public void reconnect() throws Exception {
        if (channel != null && channel.isActive()) {
            return;
        }
        // 首先清除路由信息，下线
        routeRequestService.offLine();

        echoService.echo("im server shutdown, reconnecting....");
        start();
        echoService.echo("Great! reconnect success!!!");
        reconnectManager.reconnectSuccess();
        ContextHolder.clear();
    }

    /**
     * 关闭
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        if (channel != null) {
            channel.close();
        }
    }
}
