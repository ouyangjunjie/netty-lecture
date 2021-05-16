package com.hna.example.keepalive.server;

import com.hna.example.keepalive.BaseSocketInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @author owen.yang
 */
public class MyServerInitializer extends BaseSocketInitializer {

    @Override
    protected void specialHandler(ChannelPipeline pipeline) {
        pipeline.addLast(new MyServerHandler());
    }
}
