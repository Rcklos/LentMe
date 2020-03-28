package com.rcklos.lentme.dataOld;

public class constPool {
    /**
     * Integer类型常量池
     */
    public static final int Port = 8080;

    public static final int LENGTH_USER          =  13;
    public static final int LENGTH_TYPE          =   4;
    public static final int LENGTH_MATCH         =   5;
    public static final int LENGTH_ALIVE         =  16;

    //网络通知
    public static final int NOTI_CHAT_MESSAGE   =   0;
    public static final int NOTI_CHAT_DESTROY   =   1;
    public static final int NOTI_TYPE_ALIVE     =   2;
    public static final int NOTI_TYPE_MATCHED   =   3;
    public static final int NOTI_MATCH_RECEIVE  =   4;

    /*
     * Long类型常量池
     */
    public static final long TIME_OUT = 5000;
    public static final long TIME_SLEEP = 500;

    /**
     * String类型常量池
     */
    public static final String addrServer = "192.168.0.101";

    public static final String PUBLIC_KEY   = "0000000000000000";

    /**
     * Socket 数据
     */
    public static final String SOCKET_DATA_OK     = "RECV_OK";
    public static final String SOCKET_DATA_ALIVE  = "RCK";//占用3个字节
    public static final String SOCKET_DATA_MATCH  = "MATCH";

    public static final String TAG = "DRL-";

    public static final String BYTE_CHARSET = "UTF-8";//编码默认UTF-8

}
