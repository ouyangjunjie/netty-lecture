package com.hna.example.keepalive.client;

import com.hna.example.keepalive.BaseSocketInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @author owen.yang
 */
public class MyClientInitializer extends BaseSocketInitializer {

    @Override
    protected void specialHandler(ChannelPipeline pipeline) {
        pipeline.addLast(new MyClientHandler());
    }
}
