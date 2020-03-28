package com.rcklos.lentme.clientOld;

import android.util.Log;

import com.rcklos.lentme.dataOld.constPool;
import com.rcklos.lentme.utilsOld.AESUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiver extends Receiver {

    private BufferedReader br;
    private ClientUtils utils;

    public ClientReceiver(String user, Socket socket) {
        super(user, socket);
    }

    @Override
    public void init() {
        Log.i(TAG,"Receiver初始化");
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(socket.getInputStream(), constPool.BYTE_CHARSET);
            br = new BufferedReader(isr);
            utils = RecvUtils.getInstance();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String unpackData(String data) {
        try {
            return AESUtil.decrypt(data, constPool.PUBLIC_KEY);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("user:["+user+"]receive数据解密发生错误");
        }
        return null;
    }

    @Override
    public void Rreceive() throws Exception {
        String data = br.readLine();
        if ( data != null ) {

            Log.i(TAG,"收到了的数据包：" + data);
            data = unpackData(data);
            Log.i(TAG,"解密后的数据包："+data);

            if (data==null) {
                breakReceive();
                Log.i(TAG,"解密后的数据包为空包-->服务器主动断开连接");
            }

            if ( Sender.containsRecv() ) {
                if ( !Sender.verifyRecv(data) ) {
                    breakReceive();
                    System.out.println("user:["+user+"]违反协议 data:"+data +"-->服务器主动断开连接");
                    return;
                }
            }

            utils.unpack(user,data);
        }
    }

    @Override
    public void Rdestroy() {
        try {
            Log.i(TAG,"receiver开始销毁");
            closeSocket();
            if(br!=null) {
                br.close();
            }
            Log.i(TAG,"receiver销毁完成");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean closeSocket() throws Exception {

        if( vchatClient.getInstance().isMatching() )
            vchatClient.getInstance().cancelMatch();//删除匹配队列

        breakReceive();

        if ( getSocket().isClosed() )
            getSocket().close();

        if ( socket != null ) {
            if ( socket.isClosed() ) return true;
            Log.i(TAG,"receiver未断开-->正在进行断开连接操作");
            socket.close();
            Log.i(TAG,"receiver socket已断开连接");
            return false;
        }

        return true;
    }
}
