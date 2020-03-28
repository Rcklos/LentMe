package com.rcklos.lentme.client;

import com.rcklos.lentme.protobuf.MessageProtobuf;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class ClientChannelInitializerHandler extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frameEncoder",new LengthFieldPrepender(2));
        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
        pipeline.addLast("protobufDecoder",new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));
        pipeline.addLast("protobufEncoder", new ProtobufEncoder());
        pipeline.addLast(new ClientReqHandler());
    }
}
