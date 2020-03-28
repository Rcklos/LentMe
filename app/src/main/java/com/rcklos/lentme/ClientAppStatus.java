package com.rcklos.lentme;

import android.content.Context;

import com.rcklos.lentme.activity.BaseActivity;

public class ClientAppStatus {
    private static BaseActivity currentActivity;
    private static String currentUserID;//正在使用的userid
    private static String currentChatSignToken;//当前单聊的token
    private static String signleTimerString = "00:00";//单聊窗口的倒计时
    private static long signleTimer = 0;//记录现在的单聊的时间
    private static boolean alive = false;
    private static boolean FlagMatchStart = false;

    public static BaseActivity getCurrentActivity() {
        return currentActivity;
    }
    public static void setCurrentActivity(BaseActivity currentActivity) {
        ClientAppStatus.currentActivity = currentActivity;
    }

    public static String getCurrentUserID() {
        return currentUserID;
    }

    public static void setCurrentUserID(String currentUserID) {
        ClientAppStatus.currentUserID = currentUserID;
    }

    public static String getCurrentChatSignToken() {
        return currentChatSignToken;
    }

    public static void setCurrentChatSignToken(String currentChatSignToken) {
        ClientAppStatus.currentChatSignToken = currentChatSignToken;
    }

    public static String getSignleTimerString() {
        return signleTimerString;
    }

    public static synchronized void setSignleTimerString(String signleTimerString) {
        ClientAppStatus.signleTimerString = signleTimerString;
    }

    public static long getSignleTimer() {
        return signleTimer;
    }

    public static String getSingleLastTimer(){
        //倒计时
        setSignleTimer(signleTimer+1000);
        return getSignleTimerString();
    }

    private static long getTimeMillisFromToken(String token) {
        String timeMillisString = token.split(":")[1];
        if ( timeMillisString == null || timeMillisString.isEmpty() ){
            return 0;
        }
        long timeMillis = Long.valueOf(timeMillisString);
        return timeMillis;
    }

    public static synchronized void setSignleTimer(long signleTimer) {
        ClientAppStatus.signleTimer = signleTimer;
        long timer = (1000*60*5)  - (signleTimer - getTimeMillisFromToken(getCurrentChatSignToken()));
        int minutes = (int) ((timer % 3600000 ) / 60000);
        int seconds = (int) ((timer % 60000)    / 1000 );
        setSignleTimerString(String.format("%d:%d",minutes,seconds));
        if ( timer <= 0 ) ClientTransponder.getInstance().destroySingleChat();
    }

    public static boolean isAlive() {
        return alive;
    }

    public static void setAlive(boolean alive) {
        ClientAppStatus.alive = alive;
    }

    public static boolean isFlagMatchStart() {
        return FlagMatchStart;
    }

    public static void setFlagMatchStart(boolean flagMatchStart) {
        FlagMatchStart = flagMatchStart;
    }
}
