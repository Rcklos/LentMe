package com.rcklos.lentme.client;

import com.rcklos.lentme.ClientAppStatus;
import com.rcklos.lentme.ClientConfig;
import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.protobuf.MessageProtobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyTCPClient implements NettyTCPClientInterface{

    private Bootstrap bootstrap;
    private Channel channel;
    private EventLoopGroup worker;
    private Thread aliveThread = new Thread(){
        @Override
        public void run() {
            while(ClientAppStatus.isAlive() ){
                ClientTransponder.getInstance().sendHeartBeat();
                try {
                    sleep(1000 * 15l );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void init() {
        worker = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ClientChannelInitializerHandler());
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    public void connect()  {
        try {
            channel = bootstrap.connect(ClientConfig.address,ClientConfig.port).sync().channel();
            if ( channel != null ) {
                System.out.println("已连接服务器");
                ClientAppStatus.setAlive(true);
                aliveThread.start();
            }
            else throw new InterruptedException();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("连接服务器失败");
        }
    }

    @Override
    public void exitTCP(){
        if ( channel != null ){
            worker.shutdownGracefully();
        }
    }


    @Override
    public void sendMsg( MessageProtobuf.Msg msg) {
        if ( channel == null ){
            return;
        }
        if ( msg == null ){
            return;
        }
        channel.writeAndFlush(msg);
    }

    @Override
    public MessageProtobuf.Msg generateTokenPackage() {
        return null;
    }

    @Override
    public MessageProtobuf.Msg generateHeartBeatPackage() {
        return null;
    }
}
