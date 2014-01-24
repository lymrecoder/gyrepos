package org.testxxx.itfc;

import java.util.Hashtable;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public abstract class CommSrvrHandler extends Handler {

	private Context ctx;
	
	/**
	 * ���ڵ���λ����д�÷������ڽ�������ʱ����һЩ���²���
	 * @return
	 */
	abstract public void onRecvData(int actionCode, Hashtable<String, ?> data);
	
	public void setContext(Context ctx){
		this.ctx = ctx;
	}
	
	public Context getContext(){
		return ctx;
	}
	
	//@Override
	public void handleMessage(Message msg){
		Hashtable<String, ?> recvdata;
		int actionCode;
		if(msg.what > 0){
			recvdata = (Hashtable<String, ?>) msg.getData().getSerializable("ret");
			actionCode = msg.getData().getInt("actionCode");
			// TODO ����ɽ�����֤��ȷ����Ϊ
			onRecvData(actionCode, recvdata);
		}
	}
	
	public CommSrvrHandler(){
		super();
	}
	
	public CommSrvrHandler(Context ctx){
		super();
		setContext(ctx);
	}

	
}
