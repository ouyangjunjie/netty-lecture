package com.hna.example.keepalive.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author owen.yang
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.err.println(ctx.channel().remoteAddress() + ", " + msg);
        TimeUnit.SECONDS.sleep(10);
        ctx.channel().writeAndFlush("from server : " + UUID.randomUUID());
    }
}
