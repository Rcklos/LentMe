package com.rcklos.lentme.utils;

import android.os.Handler;

import com.rcklos.lentme.ClientAppStatus;
import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.protobuf.MessageProtobuf;

public class MatchUtil {

    private static final String TAG = "MatchUtil";
    private static MatchUtil instance = new MatchUtil();
    private Handler handler = new Handler();
    private MatchUtil(){}
    private OnMatchListener onMatchListener;
    public static MatchUtil getInstance(OnMatchListener onMatchListener) {
        instance.onMatchListener = onMatchListener;
        return instance;
    }

    public static MatchUtil getInstance(){
        return instance;
    }

    public void respond(MessageProtobuf.Msg msg){
        final int sign = Integer.valueOf(msg.getHead().getExtend());
        if ( sign < 13 && sign >= 0 ){
            if (sign == 7 ){
                ClientTransponder.getInstance().showToast("对方可能暂时不在线哦！");
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    match(sign+1);
                }
            },2000);
        } else if ( sign == 1000 ){
            //匹配成功
            ClientAppStatus.setCurrentChatSignToken(msg.getBody());//保存token
            ClientAppStatus.setFlagMatchStart(false);
            onMatchListener.onMatch();
        } else {
            ClientAppStatus.setFlagMatchStart(false);
            onMatchListener.onFail();
        }
    }

    private void match(int time){
        if( time == 0 ) {
            ClientAppStatus.setFlagMatchStart(true);
            ClientTransponder.getInstance().sendMatch(time);
        } else {
            if ( ClientAppStatus.isFlagMatchStart() ){
                ClientTransponder.getInstance().sendMatch(time);
            }
        }
    }

    public void match(){
        match(0);
    }

}
