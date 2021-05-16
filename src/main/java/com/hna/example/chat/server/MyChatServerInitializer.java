package com.hna.example.chat.server;

import com.hna.example.chat.BaseChatServerInitializer;
import io.netty.channel.ChannelPipeline;

public class MyChatServerInitializer extends BaseChatServerInitializer {

    @Override
    protected void specialHandler(ChannelPipeline pipeline) {
        pipeline.addLast(new MyChatServerHandler());
    }
}
