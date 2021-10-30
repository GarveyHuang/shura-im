package com.shura.im.client.init;

import com.shura.im.client.handle.IMClientHandle;
import com.shura.im.common.protocol.IMReqMsg;
import com.shura.im.common.protocol.ObjDecoder;
import com.shura.im.common.protocol.ObjEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
public class IMClientHandleInitializer extends ChannelInitializer<Channel> {

    private final IMClientHandle imClientHandle = new IMClientHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 15秒客户端没给服务端主动发送消息就触发写空闲，执行TIMClientHandle的userEventTriggered方法给服务端发送一次心跳
                .addLast(new IdleStateHandler(0, 15, 0))
                .addLast(new ObjEncoder(IMReqMsg.class))
                .addLast(new ObjDecoder(IMReqMsg.class))
                .addLast(imClientHandle)
        ;
    }
}
