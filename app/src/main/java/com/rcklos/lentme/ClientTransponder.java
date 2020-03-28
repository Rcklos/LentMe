package com.rcklos.lentme;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.rcklos.lentme.activity.ChatActivity;
import com.rcklos.lentme.activity.MainActivity;
import com.rcklos.lentme.client.NettyTCPClient;
import com.rcklos.lentme.client.NettyTCPClientInterface;
import com.rcklos.lentme.protobuf.MessageProtobuf;
import com.rcklos.lentme.utilsOld.LogUtil;

import cn.refactor.lib.colordialog.PromptDialog;


public class ClientTransponder {
    private static final String TAG = "ClientTransponder";
    private static ClientTransponder instance;
    public static ClientTransponder getInstance() {
        if (instance==null){
            synchronized (ClientTransponder.class){
                instance = new ClientTransponder();
            }
        }
        return instance;
    }
    private NettyTCPClientInterface tcpClient;

    private static Handler handler = new Handler();


    private ClientTransponder(){
        handler = new Handler();
        tcpClient = new NettyTCPClient();
        tcpClient.init();
        tcpClient.connect();
    }
    private void sendMsg(MessageProtobuf.Msg msg){
        tcpClient.sendMsg(msg);
    }

    private MessageProtobuf.Msg generateMsg(int type,String toID,String extend,String body){
        MessageProtobuf.Msg.Builder builder = MessageProtobuf.Msg.newBuilder();
        MessageProtobuf.Head.Builder head = MessageProtobuf.Head.newBuilder();
        head.setMsgType(type);
        if ( ClientAppStatus.getCurrentUserID()==null ){
            LogUtil.i(TAG,"gen userid == null");
            return null;
        }
        head.setFromId(ClientAppStatus.getCurrentUserID());
        if ( toID != null )head.setToId(toID);
        if ( extend!=null)head.setExtend(extend);
        builder.setHead(head.build());
        if ( body != null )builder.setBody(body);
        return builder.build();
    }

    public void sendLoginMsg(String userPhone){
        sendMsg(generateMsg(ClientConfig.typeLogin,null,"token_"+userPhone,null));
    }

    public void sendHeartBeat(){
        sendMsg(generateMsg(ClientConfig.typeHeartBeat,null,null,null));
    }

    public void sendMatch( int time ){
        sendMsg(generateMsg(ClientConfig.typeMatch,null,String.valueOf(time),null));
    }

    public void sendMatchCancel(){
        ClientAppStatus.setFlagMatchStart(false);
        sendMsg(generateMsg(ClientConfig.typeMatchCancel,null,null,null));
    }

    public void sendMessageBySignToken( String message ){
        sendMsg(generateMsg(ClientConfig.typeSingleTokenMessage,ClientAppStatus.getCurrentChatSignToken(),null,message));
    }

    public void sendRequireTimer(){
        sendMsg(generateMsg(ClientConfig.typeGetSingleChatTime,null,null,null));
    }

    public void exitApp(){
        tcpClient.exitTCP();
    }


    public void post( Runnable runnable ){
        handler.post(runnable);
    }

    public void postDelay(Runnable runnable , long millis){
        handler.postDelayed(runnable,millis);
    }

    public void showToast( final String text ){
        post(new Runnable() {
            @Override
            public void run() {
                ClientAppStatus.getCurrentActivity().show(text);
            }
        });
    }

    public void setSingleTimer(long timer){
        ClientAppStatus.setSignleTimer(timer);
    }

    public void showToast(final String text , final boolean showLongTime){
        post(new Runnable() {
            @Override
            public void run() {
                if ( showLongTime ){
                    Toast.makeText(ClientAppStatus.getCurrentActivity(),text,Toast.LENGTH_LONG);
                } else {
                    ClientAppStatus.getCurrentActivity().show(text);
                }
            }
        });
    }

    public void destroySingleChat(){
        if ( ClientAppStatus.getCurrentChatSignToken() == null ) return;
        ClientAppStatus.setSignleTimer(0);
        ClientAppStatus.setCurrentChatSignToken(null);
        ClientTransponder.getInstance().sendMatchCancel();
        if ( ClientAppStatus.getCurrentActivity() instanceof ChatActivity ){
            showToast("聊天窗口要销毁了哦！",true);
            post(new Runnable() {
                @Override
                public void run() {
                    ClientAppStatus.getCurrentActivity().finish();
                }
            });
        }
    }

    public void toMainActivity(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                ClientAppStatus.getCurrentActivity().startActivity(new Intent(ClientAppStatus.getCurrentActivity(), MainActivity.class));
                ClientAppStatus.getCurrentActivity().animFade();
                ClientAppStatus.getCurrentActivity().finish();
            }
        });
    }

    /**
     * 展示dialog
     * @param text dialog文本
     * @param runnable 确认时的回调
     * @param cancel 是否带取消按键
     */
    public void showConfirmDialog(String text , final Runnable runnable , boolean cancel ){
        final PromptDialog dialog = new PromptDialog(ClientAppStatus.getCurrentActivity());
                dialog.setAnimationEnable(true)
                .setTitleText("确定吗？")
                .setDialogType(PromptDialog.DIALOG_TYPE_DEFAULT)
                .setContentText(text)
                .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog promptDialog) {
                        runnable.run();
                    }
                });
                
                dialog.show();
    }

}
