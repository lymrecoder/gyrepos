package org.testxxx.helloworld_old;

import java.util.Hashtable;

import org.testxxx.itfc.CommSrvrHandler;
import org.testxxx.itfc.CommThread;
import org.testxxx.service.SrvrComm;

import android.app.ListActivity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HelloWorldActivityHandler extends ListActivity {

	private SrvrComm.SrvrCommBinder binder;
	
	private CommSrvrHandler hd = new CommSrvrHandler(this){
		public void onRecvData(int ActionCode, Hashtable<String,?> recvdata){
			// TODO ����ı�������
			String ret = (String)recvdata.get("test");
	    	Toast.makeText(getApplicationContext(), (ret.isEmpty()?"empty ret":ret),
	    		     Toast.LENGTH_SHORT).show();
		}
	};
    
    private ServiceConnection conn_srv = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("--Service Connected--");
			binder = (SrvrComm.SrvrCommBinder)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("--Service Disconnected--");
		}
    	
    };
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] arr = { "��������", "�Ѱ�����", "�������" };
		// ����ArrayAdapter����
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_selectable_list_item, arr);
		// ���øô�����ʾ�б�
		setListAdapter(adapter);
	
		// ������ע�⣬��service��bind������ͬ���ģ��������ϳ���conn_srv��onServiceConnected����
		final Intent intent = new Intent();
    	intent.setAction("org.testxxx.service.SRVR_COMM");
    	bindService(intent, conn_srv, Service.BIND_AUTO_CREATE);
    }
    
    protected void onListItemClick (ListView l, View v, int position, long id){
		Hashtable<String, Object> data = new Hashtable();
    	CommThread ct = new CommThread(hd, binder);
		ct.registerSendData(data);
		// ��֮ǰ����ҳ����Ͻ���������onRecvData��ȥ��������
		new Thread(ct).start();
    }

//    @Override
 /*   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello_world, menu);
        return true;
    } */
    
    protected void onDestroy(){
        super.onDestroy();
        unbindService(conn_srv);
    }
}
