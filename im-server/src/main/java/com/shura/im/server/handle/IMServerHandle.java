package com.shura.im.server.handle;

import com.shura.im.common.constant.Constants;
import com.shura.im.common.exception.IMException;
import com.shura.im.common.kit.HeartBeatHandler;
import com.shura.im.common.pojo.IMUserInfo;
import com.shura.im.common.protocol.IMReqMsg;
import com.shura.im.common.util.NettyAttrUtil;
import com.shura.im.server.kit.RouteHandler;
import com.shura.im.server.kit.ServerHeartBeatHandlerImpl;
import com.shura.im.server.util.SessionSocketHolder;
import com.shura.im.server.util.SpringBeanFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@ChannelHandler.Sharable
public class IMServerHandle extends SimpleChannelInboundHandler<IMReqMsg> {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMReqMsg.class);

    /**
     * 取消绑定
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 可能出现业务判断离线后再次触发 channelInactive
        IMUserInfo userInfo = SessionSocketHolder.getUserId((NioSocketChannel) ctx.channel());
        if (userInfo != null) {
            LOGGER.warn("[{}] trigger channelInactive offline!", userInfo.getUsername());

            // 清除路由关系并下线
            RouteHandler routeHandler = SpringBeanFactory.getBean(RouteHandler.class);
            routeHandler.userOffLine(userInfo, (NioSocketChannel) ctx.channel());

            ctx.channel().close();
        }
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                HeartBeatHandler heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class);
                heartBeatHandler.process(ctx);
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMReqMsg msg) throws Exception {
        LOGGER.info("received msg = [{}]", msg.toString());

        if (Constants.CommandType.LOGIN == msg.getType()) {
            // 保存客户端与 channel 之间的关系
            SessionSocketHolder.put(msg.getRequestId(), (NioSocketChannel) ctx.channel());
            SessionSocketHolder.saveSession(msg.getRequestId(), msg.getReqMsg());

            LOGGER.info("client [{}] online success!!!", msg.getReqMsg());
        }

        // 更新心跳时间
        if (Constants.CommandType.PING == msg.getType()) {
            NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
            // 向客户端响应 pong 消息
            IMReqMsg heartBeat = SpringBeanFactory.getBean("heartBeat", IMReqMsg.class);

            ctx.writeAndFlush(heartBeat).addListeners((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    LOGGER.error("IO error, close channel~~~");
                    future.channel().close();
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (IMException.isResetByPeer(cause.getMessage())) {
            return;
        }

        LOGGER.error(cause.getMessage(), cause);
    }
}
