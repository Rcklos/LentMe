package com.rcklos.lentme.client;

import android.widget.Toast;

import com.rcklos.lentme.ClientAppStatus;
import com.rcklos.lentme.ClientConfig;
import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.protobuf.MessageProtobuf;
import com.rcklos.lentme.utilsOld.LogUtil;
import com.rcklos.lentme.utils.MatchUtil;
import com.rcklos.lentme.utils.chatUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientReqHandler extends ChannelInboundHandlerAdapter {
    private static final String TAG = "ClientReqHandler";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LogUtil.i(TAG,"channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LogUtil.i(TAG,"channelInActive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        LogUtil.i(TAG,"exceptionCaught:" + cause.getMessage() );
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.Msg message = (MessageProtobuf.Msg)msg;
        if ( message == null ) return;
        LogUtil.i(TAG, "收到封包："+message);
        switch( message.getHead().getMsgType() ){
            case ClientConfig.typeHeartBeat :
                System.out.println("收到服务器的心跳包");
                break;
            case ClientConfig.typeLogin:
                System.out.println("收到服务器的登陆包");
                ClientTransponder.getInstance().sendHeartBeat();
                ClientTransponder.getInstance().toMainActivity();
                break;
            case ClientConfig.typeRespond:
                Toast.makeText(ClientAppStatus.getCurrentActivity(),message.getHead().getExtend(),Toast.LENGTH_SHORT);
                break;
            case ClientConfig.typeMatch:
                MatchUtil.getInstance().respond(message);
                break;
            case ClientConfig.typeSingleTokenMessage:
                chatUtils.getInstance().reqMessage(message);
                break;
            case ClientConfig.typeGetSingleChatTime:
                chatUtils.getInstance().reqSingleTimer(message);
                break;
            case ClientConfig.typeInformTheSignTokenDestroy:
            case ClientConfig.typeInformTheTokenIsExpired:
                chatUtils.getInstance().reqInform(message);
                break;
        }
    }
}

