package com.hna.example.chat.client;

import com.hna.example.chat.BaseChatServerInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * @author owen.yang
 */
public class MyChatClientInitializer extends BaseChatServerInitializer {

    @Override
    protected void specialHandler(ChannelPipeline pipeline) {
        pipeline.addLast(new MyChatClientHandler());
    }
}
