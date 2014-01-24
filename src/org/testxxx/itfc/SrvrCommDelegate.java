package org.testxxx.itfc;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class SrvrCommDelegate {
    
	private Context ctx;
	private CommIBinder binder;
    
    private ServiceConnection conn_srv = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("--Service Connected--");
			binder = (CommIBinder)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("--Service Disconnected--");
		}
    	
    };

	public SrvrCommDelegate(Context ctx) {
		this.ctx = ctx;
	}
	
	public void bindService(){
		// ������ע�⣬��service��bind������ͬ���ģ��������ϳ���conn_srv��onServiceConnected����
		final Intent intent = new Intent();
    	intent.setAction("org.testxxx.service.SRVR_COMM");
    	ctx.bindService(intent, conn_srv, Service.BIND_AUTO_CREATE);
	}
	
	public CommIBinder getBinder(){
		return binder;
	}

	public void unbindService(){
		ctx.unbindService(conn_srv);
	}
}
