package com.rcklos.lentme.clientOld;

import android.util.Log;

import com.rcklos.lentme.dataOld.constPool;
import com.rcklos.lentme.dataOld.localDataPool;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class vchatClient {
    protected static String TAG = constPool.TAG + "Client";

    private static vchatClient instance;

    private ExecutorService fixPool;
    private Socket socket;
    private ClientReceiver clientReceiver;
    private boolean matching = false;

    public void execute( Runnable runnable ){
        fixPool.execute(runnable);
    }

    private void realInit() throws Exception{
        if ( socket!= null ){
            socket.close();
            Thread.sleep(200);
        }
        if ( clientReceiver!=null ){
            clientReceiver.breakReceive();
            Thread.sleep(200);
        }
        if(localDataPool.getInstance().getLocalUser()==null){
            Log.i(TAG,"User=null");
            return;
        }

        socket = new Socket(constPool.addrServer, constPool.Port);
        if ( socket== null ){
            Log.i(TAG,"socket连接失败！");
            return;
        }
        clientReceiver = new ClientReceiver(localDataPool.getInstance().getLocalUser(),socket);
        clientReceiver.start();
        execute(new ClientSender(localDataPool.getInstance().getLocalUser(),constPool.NOTI_TYPE_ALIVE,null));
    }

    public Socket getSocket(){
        return socket;
    }

    public vchatClient init() {
        execute(new Runnable() {
            @Override
            public void run() {
                try {
                    realInit();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG,"Socket init异常");
                }
            }
        });
        return null;
    }

    public synchronized boolean cancelMatch(){
        if ( !isMatching() ){
            Log.i( TAG , "cancelMatch失败-->matching = false");
            return false;
        }
        //还没完成
        matching = false;
        return true;
    }

    public synchronized boolean startMatch(){
        if ( isMatching() ){
            Log.i( TAG , "startMatch-->matching = true");
            return false;
        }
        //还没完成
        matching = true;
        return false;
    }

    public boolean isMatching(){
        return matching;
    }

    private vchatClient(){
        fixPool = Executors.newFixedThreadPool(4);
    }
    public static vchatClient getInstance() {
        synchronized (vchatClient.class){
            if (null == instance){
                instance = new vchatClient();
            }
        }
        return instance;
    }
}
