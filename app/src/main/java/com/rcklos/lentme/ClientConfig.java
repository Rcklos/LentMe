package com.rcklos.lentme;

public class ClientConfig {
    public static final String address = "lentme.ink";
    public static final int port = 8080;

    public static final int typeHeartBeat = 1001;
    public static final int typeLogin     = 1002;
    public static final int typeMatch     = 1003;
    public static final int typeSingleTokenMessage = 1004;
    public static final int typeMatchCancel = 3001;
    public static final int typeRespond   = 2000;
    public static final int typeGetSingleChatTime = 2001;
    public static final int typeInformTheTokenIsExpired = 20002;
    public static final int typeInformTheSignTokenDestroy = 20003;
}
