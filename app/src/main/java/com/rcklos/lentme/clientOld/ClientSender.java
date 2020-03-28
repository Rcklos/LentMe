package com.rcklos.lentme.clientOld;

import android.util.Log;

import com.rcklos.lentme.dataOld.constPool;
import com.rcklos.lentme.utilsOld.AESUtil;

import java.net.Socket;

public class ClientSender extends Sender{

    private boolean packAliveData() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( user );
        stringBuffer.append( constPool.SOCKET_DATA_ALIVE );
        data = stringBuffer.toString();
        return true;
    }

    private boolean encrypt(String formalData) {
        StringBuffer stringBuffer;
        try {
            data = AESUtil.encrypt(formalData, constPool.PUBLIC_KEY);
            //data = AesUtil.aesEncrypt(formalData,constPool.PUBLIC_KEY);
            stringBuffer = new StringBuffer();
            stringBuffer.append(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG,"sender封包加密失败");
            return false;
        }
        stringBuffer.append( "\n" );
        data = stringBuffer.toString();
        return true;
    }

    @Override
    public Socket getSocket() {
        socket = vchatClient.getInstance().getSocket();
        return socket;
    }

    @Override
    public String packData() {

        switch(type) {
            case constPool.NOTI_TYPE_ALIVE:
                packAliveData();
                break;
//            case constPool.NOTI_TYPE_MATCHED:
//                if ( !packMatchData() )
//                    data = null;
//                break;
            default:data = null;
        }

        Log.i(TAG,"第一次打包data: " + data );

        try {
            Log.i(TAG,AESUtil.encrypt(data,constPool.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"加密错误");
        }

        if ( !encrypt(data) ){
            try {
                closeSocket();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        Log.i(TAG,"第二次打包data: " + data );

        try {
            Log.i(TAG,AESUtil.decrypt(data,constPool.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"解密错误");
        }

        return data;

    }

    @Override
    public boolean closeSocket() throws Exception {

        Log.i(TAG,"sender closeSocket");

        if (socket==null){
            Log.i(TAG,"sender closeSocket时已经连接失效");
            return true;
        } else if (socket.isClosed()){
            Log.i(TAG,"sender closeSocket时已经连接关闭");
            return true;
        }

        socket.close();

        if ( osw!=null ){
            osw.close();
        }

        return true;
    }

    public ClientSender(String user, int type) {
        super(user, type);
    }

    public ClientSender(String user, int type, String data) {
        super(user, type, data);
    }

    public ClientSender(String user, int type, String data, String recvData) {
        super(user, type, data, recvData);
    }

    public ClientSender(String user, int type, long delayTime, executeCondition condition) {
        super(user, type, delayTime, condition);
    }

    public ClientSender(String user, int type, long delayTime, executeCondition condition, String data) {
        super(user, type, delayTime, condition, data);
    }

    public ClientSender(String user, int type, long delayTime, executeCondition condition, String data, String recvData) {
        super(user, type, delayTime, condition, data, recvData);
    }

}
