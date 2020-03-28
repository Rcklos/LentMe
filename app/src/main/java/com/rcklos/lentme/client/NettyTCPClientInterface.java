package com.rcklos.lentme.client;

import com.rcklos.lentme.protobuf.MessageProtobuf;

public interface NettyTCPClientInterface {

    /**
     * 初始化NettyTCPClient
     */
    public void init();

    /**
     * 连接服务器
     */
    public void connect();

    public void exitTCP();

    /**
     * 发送消息
     * @param msg 消息内容
     */
    public void sendMsg( MessageProtobuf.Msg msg);

    /**
     * 生成登录握手包
     */
    public MessageProtobuf.Msg generateTokenPackage();

    /**
     * 生成心跳包
     */
    public MessageProtobuf.Msg generateHeartBeatPackage();

}
