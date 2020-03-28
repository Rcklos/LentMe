package com.rcklos.lentme.clientOld;

import android.util.Log;

import com.rcklos.lentme.dataOld.constPool;

import java.net.Socket;

public abstract class Receiver extends Thread {
	protected static String TAG = constPool.TAG + "Recver";

	String user;
	Socket socket;
	
	boolean RUN_RECEIVER = true;
	boolean destroy = false;
	
	public Receiver(String user , Socket socket) {
		this.user = user;
		this.socket = socket;
	}
	public abstract void init();
	public abstract void Rreceive() throws Exception;
	public abstract void Rdestroy();
	public abstract boolean closeSocket() throws Exception ;
	
	
	public void run() {
		init();
		while( RUN_RECEIVER ) {
			if(socket.isClosed()) {
				//Log.i()
				break;
			}
			try {
				Rreceive();
				Thread.sleep(constPool.TIME_SLEEP);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG,"已终止ֹreceive");
				destroy = true;
				Rdestroy();
			} 
		}
		if ( !destroy ) Rdestroy();
	}
	
	public void breakReceive() {
		this.RUN_RECEIVER = false;
	}

	public boolean isBreakReceive(){
		return RUN_RECEIVER;
	}
	
	public void setUser( String user ) {
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
