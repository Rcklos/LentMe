package com.rcklos.lentme.clientOld;

import android.util.Log;

import com.rcklos.lentme.dataOld.constPool;

import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class Sender implements Runnable{

    protected static String TAG = constPool.TAG + "Sender";

    private static boolean queueSender = false;
    public static String queueRecver;

    String user;
    int type;
    long delayTime;
    executeCondition condition;
    Socket socket;
    String data;
    String recvData;
    OutputStreamWriter osw;

    public synchronized static boolean putSender() {
        if ( queueSender ) return false;
        else queueSender = true;
        return true;
    }

    public synchronized static boolean removeSender() {
        if ( !queueSender ){
            Log.i(TAG , "queueSender 非法赋值");
            return false;
        }
        queueSender = false;
        return true;
    }

    public static boolean containsRecv() {
        if ( null == queueRecver || queueRecver.equals("") ) return false;
        return true;
    }

    public synchronized static boolean removeRecv() {
        queueRecver = null;
        return true;
    }

    public static boolean verifyRecv( String recvData ) {
        if ( !containsRecv() ) {
            Log.e(TAG,"Recv非法验证" );
            return false;
        }
        if ( !queueRecver.equals(recvData) ) {
            Log.i(TAG,"Recv验证失败-->["+queueRecver+"]与["+recvData+"]不匹配" );
            return false;
        }
        Log.i(TAG,"Recv验证成功-->recvData:"+recvData );
        if ( removeRecv() )
            return true;
        else {
            Log.i(TAG,"Recv验证后移除队列异常" );
            return false;
        }
    }

    public synchronized static boolean putRecv( String recvData ) {
        if ( null != queueRecver || !queueRecver.equals("") ) {
            Log.i(TAG,"已存在Recv队列" );
            return false;
        }
        queueRecver = recvData;
        return true;
    }

    public boolean send( Socket socket , String data ) throws Exception{
        Long intoTime = System.currentTimeMillis();
        if ( socket.isClosed() ) {
            Log.i(TAG,"连接已失效");
            return false;
        }
        while( !putSender() ) {
            if ( System.currentTimeMillis()-intoTime > constPool.TIME_OUT ) {
                System.out.println("user:["+user+"] sender等待队列超时");
            }
            return false;
        }
        osw = new OutputStreamWriter(socket.getOutputStream(),constPool.BYTE_CHARSET);
        osw.write(data);
        osw.flush();
        Log.i( TAG ,"sender发送成功-->Time：" + (System.currentTimeMillis()-intoTime) );
        removeSender();
        return true;
    }

    public abstract Socket getSocket();
    public abstract String packData();

    public abstract boolean closeSocket() throws Exception ;
    public void run() {
        try {
            if ( delayTime != 0 ) Thread.sleep(delayTime);
            if ( condition != null )
                if ( !condition.condition() )
                    return;
            socket = getSocket();
            if ( socket == null ) {
                Log.i( TAG , "非法socket=null");
                closeSocket();
                return;
            }
            data = packData();
            if ( data == null ) {
                Log.i( TAG , "非法data=null");
                closeSocket();
                return;
            }
            if( send( socket , data ) ) {
                if ( recvData!= null ) {
                    putRecv(recvData);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                closeSocket();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    interface executeCondition{
        boolean condition();
    }

    public Sender( String user , int type ) {
        this( user , type , 0 , null , null );
    }

    public Sender( String user, int type , String data ) {
        this( user , type , 0 , null , data  );
    }

    public Sender( String user, int type , String data , String recvData ) {
        this( user , type , 0 , null , data , recvData );
    }

    public Sender( String user, int type , long delayTime , executeCondition condition ) {
        this( user , type , delayTime , condition , null  );
    }

    public Sender( String user, int type , long delayTime , executeCondition condition , String data ) {
        this( user , type , delayTime , condition , data , null );
    }

    public Sender( String user , int type , long delayTime , executeCondition condition , String data , String recvData ) {
        this.user   = user;
        this.type   = type;
        this.delayTime = delayTime;
        this.condition = condition;
        this.data   = data;
        this.recvData = recvData;
        Log.i(TAG,"sender init");
    }

}
