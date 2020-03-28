package com.rcklos.lentme.dataOld;

import java.util.Random;

public class localDataPool {
    private static localDataPool instance;

    private Random random;
    private String localUser;

    public void init(){
        random = new Random();
        createUser();
    }

    public boolean createUser(){
        synchronized (localDataPool.class){
            String user = String.valueOf(System.currentTimeMillis());
            if ( user.length() > constPool.LENGTH_USER ){
                user = user.substring(0,constPool.LENGTH_USER);
            }
            this.localUser = user;
        }
        return true;
    }

    private localDataPool(){}
    public static localDataPool getInstance() {
        synchronized (localDataPool.class){
            if ( null == instance ){
                instance = new localDataPool();
            }
        }
        return instance;
    }

    public String getLocalUser(){
        return localUser;
    }

}
