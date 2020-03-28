package com.rcklos.lentme.utils;

import android.text.TextUtils;

import com.rcklos.lentme.ClientAppStatus;
import com.rcklos.lentme.ClientConfig;
import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.activity.ChatActivity;
import com.rcklos.lentme.protobuf.MessageProtobuf;
import com.rcklos.lentme.utilsOld.LogUtil;

public class chatUtils {
    private static final String TAG = "chatUtils";
    private static chatUtils instance = new chatUtils();
    private chatUtils(){}

    private MsgHandler handler;
    private ChatActivity activity;

    public void setHandler(MsgHandler handler){
        this.handler = handler;
    }

    public static chatUtils getInstance() {
        return instance;
    }
    public void loadActivity( ChatActivity activity ){
        this.activity = activity;
    }


    /**
     * 收到消息的处理函数
     * @param msg 收到的消息
     */
    public void reqMessage(MessageProtobuf.Msg msg){
        final String message = msg.getBody();
        if (TextUtils.isEmpty(message) ){
            //收到空消息就代表“出事了”
            LogUtil.i(TAG,"收到了空消息");
            // 与UI交互必须post
            ClientTransponder.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    handler.destoryChat();
                }
            });
        } else {
            // 与UI交互必须post
            ClientTransponder.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    handler.insertMsgFromToken(message);
                }
            });
        }
    }

    public void reqSingleTimer( MessageProtobuf.Msg msg) {
        String extence = msg.getHead().getExtend();
        if ( !TextUtils.isEmpty(extence) ){
            ClientTransponder.getInstance().setSingleTimer(Long.valueOf(extence));
        }
    }

    public void reqInform( MessageProtobuf.Msg msg ){
        if (ClientAppStatus.getCurrentChatSignToken()==null) return;
        switch (msg.getHead().getMsgType()){
            case ClientConfig.typeInformTheSignTokenDestroy:
                ClientTransponder.getInstance().showToast("对方已经销毁了此聊天窗口!");
                ClientTransponder.getInstance().destroySingleChat();
                break;
            case ClientConfig.typeInformTheTokenIsExpired:
                ClientTransponder.getInstance().showToast("该聊天窗口已经过期了哦!");
                ClientTransponder.getInstance().destroySingleChat();
                break;
        }
    }

    public void sendMessgae(String message){
        ClientTransponder.getInstance().sendMessageBySignToken(message);
    }

    //    public chatUtils(final Context context){
//        this.context = context;
//        handler = new HttpHandler(context){
//            @Override
//            protected void succeed(String data) {
////                super.succeed(data);
//                if ( data.contains("error")){
//                    Log.e("TAG" , data );
//                    Toast.makeText( context , data , Toast.LENGTH_LONG ).show();
//                    result.onFail(data);
//                } else {
//                    //可以更新正则表达式
//                    if ( data.equals("Chat ends")){
//                        result.onFail(data);
//                    } else {
//                        if ( mode == 128 ){
//                            result.onSucceed(data.replace("msgSucceeed",""));
//                        } else if ( mode == 124 ){
//                            result.onSucceed(data);
//                        }
//                    }
//                }
//            }
//        };
//    }

//    public void getMessage(losResult result){
//        this.result = result;
//        mode = 128;
//        ArrayList<losNameValue> losNameValues = new ArrayList<>();
//        losNameValues.add(new losNameValue("mode", "128"));
//        losNameValues.add(new losNameValue("uid", MatchUtil. uid));
//        losNameValues.add(new losNameValue("cuid",MatchUtil.cuid));
////        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
////        nameValuePairs.add(new BasicNameValuePair("mode","128"));
////        nameValuePairs.add(new BasicNameValuePair("uid", MatchUtil. uid));
////        nameValuePairs.add(new BasicNameValuePair("cuid",MatchUtil.cuid));
//        String url = URL + losNameValue.getPara(losNameValues);
//        new HttpConnectionUtils(handler).get(url);
//    }

//    public void sendMessage( String data , losResult losResult ){
//        this.result = losResult;
//        ArrayList<losNameValue> losNameValues = new ArrayList<>();
//        mode = 124;
//        try {
//            data = URLEncoder.encode(data,"UTF-8");
//            losNameValues.add(new losNameValue("mode", "124"));
//            losNameValues.add(new losNameValue("uid", MatchUtil. uid));
//            losNameValues.add(new losNameValue("cuid",MatchUtil.cuid));
//            losNameValues.add(new losNameValue("data" , data ));
//            losNameValues.add(new losNameValue("tuid",MatchUtil.tuid));
////        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
////        nameValuePairs.add(new BasicNameValuePair("mode","124"));
////        nameValuePairs.add(new BasicNameValuePair("uid", MatchUtil. uid));
////        nameValuePairs.add(new BasicNameValuePair("cuid",MatchUtil.cuid));
////        nameValuePairs.add(new BasicNameValuePair("data" , data ));
//            String url = URL + losNameValue.getPara(losNameValues);
//            new HttpConnectionUtils(handler).get(url);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

    public interface MsgHandler{
        public void insertMsgFromToken(String msg);
        public void destoryChat();
    }

}
