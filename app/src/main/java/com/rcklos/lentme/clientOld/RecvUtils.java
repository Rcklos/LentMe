package com.rcklos.lentme.clientOld;

import android.util.Log;

import com.rcklos.lentme.dataOld.constPool;
import com.rcklos.lentme.utilsOld.AESUtil;

public class RecvUtils extends ClientUtils {

    private static RecvUtils instance;

    private RecvUtils(){}

    public static RecvUtils getInstance(){
        synchronized (RecvUtils.class){
            if ( null == instance ){
                instance = new RecvUtils();
            }
        }
        return instance;
    }

    private String getObjectUserFromData(String data) {
        String muser = getUserFromData(data);
        return muser;
    }

    private String chipRecvUserInData( String user , String data ) {
        if ( data == null ) {
            Log.i(TAG,"user:["+user+"] [NULL] 拆包失败-->chipRecvUserInData-data:"+data);
            return null;
        }
        return data.replace(getObjectUserFromData(data), "");
    }

    private String chipTypeInData( String user , String data ) {
        if ( data == null ) {
            Log.i(TAG,"user:["+user+"] [NULL] 拆包失败-->chipTypeInData-data:null");
            return null;
        } else if ( data.length() < 4 ) {
            Log.i(TAG,"user:["+user+"] [Length < 4]拆包失败-->chipTypeInData-data:"+data);
            return null;
        }
        String realData = data.replace(data.substring(data.length()-4), "");
        Log.i(TAG,"user:["+user+"] 拆包成功-->chipTypeInData-data:"+realData);
        return realData;
    }

    private boolean analyseMessage(String user , String data , messageStruct msgStrcut) throws Exception {
        String chipData = chipTypeInData(user, data);
        if ( chipData == null ) {
            Log.i(TAG,"user:["+user+"] 拆包失败-->analyseMessage-chipData:null");
            return false;
        }
        String sender = getObjectUserFromData(chipData);
        if (sender==null) {
            Log.i(TAG,"user:["+user+"] 拆包失败-->analyseMessage-sender:null");
            return false;
        } else {
            msgStrcut.putRecver(sender);
            chipData = chipRecvUserInData(user, chipData);
        }
        String message = AESUtil.decrypt(chipData, sender);
        if ( message == null ) {
            Log.i(TAG,"user:["+user+"] 拆包失败-->analyseMessage-message:"+message);
            return false;
        } else {
            msgStrcut.putMessage( message );
        }
        return true;
    }

    @Override
    public boolean aliveAction(String user, String data) {
        vchatClient.getInstance().execute(new ClientSender(user,constPool.NOTI_TYPE_ALIVE,null));
        Log.i(TAG,"user:["+user+"]发送心跳包");
        return true;
    }

    @Override
    public boolean matchAction(String user) {
        return false;
    }

    @Override
    public boolean msgAction(Object object) {
        //messageStruct msgStruct = (messageStruct)object;
        return false;
    }

    @Override
    public String getUserInLogin(String data) {
        return null;
    }

    @Override
    public boolean unpack(String user, String data) throws Exception {
        Log.i(TAG,"user:["+user+"]数据包长："+data.length());

        if ( data.length() == constPool.LENGTH_ALIVE ) {
            Log.i(TAG,"user:["+user+"]解析为心跳包："+data);
            aliveAction(user,data);
            return true;
        }

        if ( data.length() == constPool.LENGTH_MATCH ) {
            Log.i(TAG,"user:["+user+"]解析为匹配包："+data);
            matchAction(user);
            return true;
        }

        int type = getDataType(data);
        switch( type ) {
            case constPool.NOTI_CHAT_MESSAGE:
                final messageStruct msgStruct = new messageStruct(user);
                if ( !analyseMessage(user,data,msgStruct) )
                    Log.i(TAG,"user["+user+"]信息数据包解析失败-->data="+data);
                msgAction(msgStruct);
                break;
            default :
                Log.i(TAG,"user["+user+"]数据类型解析失败-->type="+type);
                return false;
        }

        return true;
    }

    class messageStruct{
        String senderUser;
        String recverUser;
        String message;

        public messageStruct(String senderUser) {
            this(senderUser,null);
        }

        public messageStruct(String senderUser,String recverUser) {
            this(senderUser,recverUser,null);
        }

        public messageStruct(String senderUser,String recverUser,String message) {
            this.senderUser = senderUser;
            this.recverUser = recverUser;
            this.message    = message;
        }

        public boolean putSender( String senderUser ) {
            this.senderUser = senderUser;
            return true;
        }

        public boolean putRecver( String recverUser ) {
            this.recverUser = recverUser;
            return true;
        }

        public boolean putMessage( String message ) {
            this.message = message;
            return true;
        }

        public boolean checkMessage() {
            if ( senderUser != null && recverUser!=null ) {
                if ( message != null ) {
                    return true;
                }
            }
            System.out.println("Message数据包有误! message:"+toString());
            return false;
        }

        public String toEncryptString() {
            try {
                return AESUtil.encrypt(toString(),constPool.PUBLIC_KEY);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(senderUser);//服务器分析时，要把sender信息发给recver
            //stringBuffer.append(recverUser);//客户端发送时，要把recver信息发给server
            try {
                stringBuffer.append(AESUtil.encrypt(message, senderUser));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }
    }

}
