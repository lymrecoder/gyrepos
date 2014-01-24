
package org.testxxx.helloworld;

import java.util.ArrayList;
import java.util.Hashtable;

import org.testxxx.itfc.CommAsyncTask;
import org.testxxx.itfc.CommIBinder;
import org.testxxx.itfc.SrvrCommDelegate;
import org.testxxx.service.PushService;
import org.testxxx.util.SharePre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class TaskCategoryActivity extends Activity implements org.testxxx.helloworld.PathView.OnItemClickListener{// ��ʱ�����Ȳ��̳�commactivity��

	private CommAsyncTask commasynctask;
	private SrvrCommDelegate scd;
	private ListView listsmple;
	private TaskCategoryActivity tc;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.task_category);
        //requestWindowFeature(R.layout.task_category);
        String[] arr = { "��������", "�Ѱ�����", "�������","ͨѶ¼" };
		// ����ArrayAdapter����
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_selectable_list_item, arr);
		listsmple = (ListView) findViewById(R.id.tasklist);
		// ���øô�����ʾ�б�
		listsmple.setAdapter(adapter);
		//���һ��������Ϣ�İ�ť
		//PushService.subscribeMsg("username");
		//������Ϣ
		//----------------------------------������Ϣ��ʼ------------------------//
		SharedPreferences userinfo = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String role_level = userinfo.getString("ROLE_LEVEL", "employee");
		String username = userinfo.getString("USERNAME", "guest");
		String realname = userinfo.getString("REALNAME", "�ÿ�");
		System.out.println("----------realname------sub------"+realname);
		PushService.subscribeMsg(username);
        PushService.subscribeMsg(realname);
         if(role_level.equals("manager")){
        	 //����leader��
        	 PushService.subscribeMsg(PushService.NET_LEADER_GROUP);
         }else{
        	//����������
        	 PushService.subscribeMsg(PushService.NET_MASTER_GROUP);
         }
         //���Ŀͻ���AI
         PushService.subscribeMsg(PushService.MQTT_CLIENT_AI_ID);
         //----------------------------------������Ϣ����------------------------//
         
         tc = this;
         listsmple.setOnItemClickListener(new OnItemClickListener() {
        	 @Override  
             public void onItemClick(AdapterView<?> adapter, View view,   
                     int position,long arg3) {  
        		 if(position==3){
        	    		//��ת����Ϣ
        	    		System.out.println("-------------3-------position--------");
        	    		Intent intent = new Intent();
        				intent.setAction("org.testxxx.helloworld.GroupMessageActivity");
        				startActivity(intent);
        				finish();
        	    	}else{
        	    		Hashtable<String, Object> senddata = new Hashtable<String, Object>();
        	         	CommIBinder binder = scd.getBinder();
        	        	senddata.put("action", "2");
        	        	senddata.put("method", "1");
        	        	senddata.put("taskcategory", position);
        	        	commasynctask = new CommAsyncTask(tc , binder){
        	    			@SuppressWarnings("unchecked")
        	    			@Override
        	    			public void onRecvData(Hashtable<String, Object> recvData) {
        	    		    /*	Toast.makeText(getApplicationContext(), (recvData.isEmpty()?"empty ret":recvData.toString()),
        	    		    		     Toast.LENGTH_SHORT).show(); */
        	    		    	Bundle bd_task_list = new Bundle();
        	    		    	bd_task_list.putSerializable("list", (ArrayList<Hashtable<String, String>>)recvData.get("retval"));
        	    		    	Intent intent = new Intent();
        	    		    	intent.putExtras(bd_task_list);
        	    				intent.setAction("org.testxxx.helloworld.TaskList");
        	    				startActivity(intent);
        	    			}
        	        	};
        	        	commasynctask.execute(senddata);
        	    	}
                   
             } 
		});
         
        //���½�Ĵָ����
         setupView();
       //����activity�б�
         CloseActivityClass.activityList.add(this);
		scd = new SrvrCommDelegate(this);
		scd.bindService();
    }
    
    private void setupView() {
		PathView mPathView = (PathView) findViewById(R.id.mPathView);
		ImageButton startMenu = new ImageButton(this);
		startMenu.setBackgroundResource(R.drawable.start_menu_btn);
		mPathView.setStartMenu(startMenu);

		int[] drawableIds = new int[] { R.drawable.start_menu_scan_normal,
				R.drawable.start_menu_call_normal,
				R.drawable.start_menu_sms_normal,
				R.drawable.start_menu_chat_normal };
		View[] items = new View[drawableIds.length];
		for (int i = 0; i < drawableIds.length; i++) {
			ImageButton button = new ImageButton(this);
			button.setBackgroundResource(drawableIds[i]);
			items[i] = button;
		}
		mPathView.setItems(items);
		mPathView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(View view, int position) {
		Intent intent = new Intent();
		if(position==3){
			intent.setAction("org.testxxx.helloworld.GroupMessageActivity");
			startActivity(intent);
		}else if(position==2){
			Intent login = new Intent(this,TaskCategoryActivity.class);
			this.startActivity(login);
			//intent.setAction("org.testxxx.helloworld.TaskCategoryActivity");
			//startActivity(intent);
		}else if(position==1){
			SharedPreferences shared = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			SharePre sp = new SharePre(shared);
			sp.setBoolean("AUTO_ISCHECK", false);
			CloseActivityClass.exitClient(this);
		}else{
			Toast.makeText(this, "�������˵�"+position+"��", Toast.LENGTH_SHORT).show();
		}
	}
    
    
    /*protected void onListItemClick (ListView l, View v, int position, long id){

		  ��ʱע��
	 	Intent intent = new Intent();
		intent.setAction("org.testxxx.helloworld.TaskList");
		startActivity(intent);
    	
    	return;  
    	if(position==3){
    		//��ת����Ϣ
    		System.out.println("-------------3-------position--------");
    		Intent intent = new Intent();
			intent.setAction("org.testxxx.helloworld.GroupMessageActivity");
			startActivity(intent);
			finish();
    	}else{
    		Hashtable<String, Object> senddata = new Hashtable<String, Object>();
         	CommIBinder binder = scd.getBinder();
        	senddata.put("action", "2");
        	senddata.put("method", "1");
        	senddata.put("taskcategory", position);
        	commasynctask = new CommAsyncTask(this, binder){
    			@SuppressWarnings("unchecked")
    			@Override
    			public void onRecvData(Hashtable<String, Object> recvData) {
    		    	Toast.makeText(getApplicationContext(), (recvData.isEmpty()?"empty ret":recvData.toString()),
    		    		     Toast.LENGTH_SHORT).show();
    		    	Bundle bd_task_list = new Bundle();
    		    	bd_task_list.putSerializable("list", (ArrayList<Hashtable<String, String>>)recvData.get("retval"));
    		    	Intent intent = new Intent();
    		    	intent.putExtras(bd_task_list);
    				intent.setAction("org.testxxx.helloworld.TaskList");
    				startActivity(intent);
    			}
        	};
        	commasynctask.execute(senddata);
    	}
    	
    }*/

//    @Override
 /*   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello_world, menu);
        return true;
    } */
    
    protected void onDestroy(){
        super.onDestroy();
        scd.unbindService();
    }
}
