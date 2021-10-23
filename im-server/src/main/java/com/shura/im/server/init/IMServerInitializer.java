package com.shura.im.server.init;

import com.shura.im.common.protocol.IMReqMsg;
import com.shura.im.common.protocol.ObjDecoder;
import com.shura.im.common.protocol.ObjEncoder;
import com.shura.im.server.handle.IMServerHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: IM Server 初始化
 */
public class IMServerInitializer extends ChannelInitializer<Channel> {

    private final IMServerHandle imServerHandle = new IMServerHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 20 秒没有收到客户端发送消息或者心跳触发读空闲，执行 IMServerHandle 的 userEventTriggered 方法关闭客户端连接
                .addLast(new IdleStateHandler(20, 0, 0))
                .addLast(new ObjEncoder(IMReqMsg.class))
                .addLast(new ObjDecoder(IMReqMsg.class))
                .addLast(imServerHandle);
    }
}
