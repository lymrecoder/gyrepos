package org.testxxx.helloworld;

import java.util.Hashtable;

import org.testxxx.itfc.CommAsyncTask;
import org.testxxx.itfc.CommIBinder;
import org.testxxx.itfc.SrvrCommDelegate;
import org.testxxx.service.PushService;
import org.testxxx.util.SharePre;
import org.testxxx.util.UserSession;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText username, password;  
    private CheckBox saveInfo, autoLogin;  
    private String usernameValue,passwordValue;
    //private Button loginBtn;
    private SharedPreferences shared; 
    private SharePre sp;
    private SharePre pushSP;
    private CommAsyncTask commasynctask;
	private SrvrCommDelegate scd;
	//�豸id
	private String mDeviceID;
   
	/**
	 * ��������service
	 */
	private void startGpsService(){
		Intent intent = new Intent();
    	intent.setAction("org.testxxx.service.GPS_DETECT");
    	startService(intent);
	}
	
    private void startCommService(){
		Intent intent_srvr = new Intent();
		intent_srvr.setAction("org.testxxx.service.SRVR_COMM");
    	startService(intent_srvr);
	}
    
    /*private void stopCommService(){
    	Intent intent_srvr = new Intent();
		intent_srvr.setAction("org.testxxx.service.SRVR_COMM");
    	stopService(intent_srvr);
    }*/
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ȥ������
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        startCommService();
        //��ȡ��ʵ������
        shared = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences pushServ = this.getSharedPreferences(PushService.TAG, MODE_PRIVATE);
        sp = new SharePre(shared);  
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        autoLogin = (CheckBox) findViewById(R.id.autoLoginChk);
    	saveInfo = (CheckBox) findViewById(R.id.saveInfoChk);
    	//loginBtn = (Button) findViewById(R.id.loginBtn);
    	//��ȡ�豸id,���浽�����ļ���
    	mDeviceID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
    	sp.setString("mDeviceID", mDeviceID);
    	UserSession.set("mDeviceID", mDeviceID);
    	pushSP = new SharePre(pushServ);
    	pushSP.setString(PushService.PREF_DEVICE_ID, mDeviceID);
    	UserSession.set(PushService.PREF_DEVICE_ID, mDeviceID);
    	//������Ϣ���ͷ���
        PushService.actionStart(getApplicationContext());
    	//�жϼ�ס�����ѡ���״̬
    	if(sp.getBoolean("SAVE_ISCHECK", false)){
    		//���������ѡ��Ϊ��ס
    		saveInfo.setChecked(true);
    		username.setText(sp.getString("USERNAME", ""));
    		password.setText(sp.getString("PASSWORD", ""));
        	UserSession.set("USERNAME", sp.getString("USERNAME", ""));
        	UserSession.set("PASSWORD", sp.getString("PASSWORD", ""));
    		//�ж��Զ���¼��ѡ��״̬
    		if(sp.getBoolean("AUTO_ISCHECK", false)){
    			//����Ĭ�ϵĵ�¼״̬
    			autoLogin.setChecked(true);
    			
    	//		startGpsService();
    			//��ת���ڵ�¼����
    			/*Intent intent = new Intent();
		        intent.setAction("org.testxxx.helloworld.TaskCategoryActivity");
		        startActivity(intent);*/
    			Intent login = new Intent(MainActivity.this,TaskCategoryActivity.class);
    			MainActivity.this.startActivity(login);
    		}
    	} 
    	
    	 // ��¼�����¼�  ����Ĭ��Ϊ�û���Ϊ��liu ���룺123  
/*        loginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {  
               
            }  
        });
        */ 
        
        //������ס�����ѡ��ť�¼�  
        saveInfo.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (saveInfo.isChecked()) {
                    System.out.println("��ס������ѡ��"); 
                    sp.setBoolean("SAVE_ISCHECK", true);
                }else {
                    System.out.println("��ס����û��ѡ��");  
                    sp.setBoolean("SAVE_ISCHECK", false);
                }
            }  
        });
        
      //�����Զ���¼��ѡ��ť�¼�  
        autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (autoLogin.isChecked()) {
                    System.out.println("�Զ���¼��ѡ��");
                    sp.setBoolean("AUTO_ISCHECK", true);
                }else {
                    System.out.println("�Զ���¼û��ѡ��");
                    sp.setBoolean("AUTO_ISCHECK", false);
                }
            }  
        });
        //����activity�б�
        CloseActivityClass.activityList.add(this);
        scd = new SrvrCommDelegate(this);
		scd.bindService();  
    }
    
    //��ť����¼�
    public void doLogin(View v){
    	usernameValue = username.getText().toString();  
        passwordValue = password.getText().toString();
        Hashtable<String, Object> senddata = new Hashtable<String, Object>();
        CommIBinder binder = scd.getBinder();
     	senddata.put("username", usernameValue);
     	senddata.put("password", passwordValue);
     	System.out.println("username----------"+usernameValue);
     	System.out.println("password----------"+passwordValue);
     	senddata.put("action", ActionType.LOGIN);
     	senddata.put("method", ActionType.CHECK_LOGIN);
     	commasynctask = new CommAsyncTask(this, binder){
 			@Override
 			public void onRecvData(Hashtable<String, Object> recvData) {
 		    	//Toast.makeText(getApplicationContext(), (recvData.isEmpty()?"empty ret":recvData.toString()),
 		    		    // Toast.LENGTH_SHORT).show();
 		    	String isLogin = recvData.get("isLogin").toString();
 		    	if(isLogin.equals("yes")){  
 		             Toast.makeText(MainActivity.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();  
 		             //��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ  
 		             if(saveInfo.isChecked()){
 		             	//��ס�û��������롢
 		             	sp.setString("USERNAME", usernameValue);
 		             	sp.setString("PASSWORD", passwordValue); 
 		             } 
		             UserSession.set("USERNAME", usernameValue);
		             UserSession.set("PASSWORD", passwordValue);
 		             String role_level = recvData.get("role_level").toString();
 		             String uid = recvData.get("uid").toString();
 		             String realname = recvData.get("realname").toString();
 		             //����Ȩ��
 		             sp.setString("ROLE_LEVEL", role_level);
 		             sp.setString("UID", uid);
 		             sp.setString("REALNAME", realname);
		             UserSession.set("ROLE_LEVEL", role_level);
		             UserSession.set("REALNAME", realname);
		             UserSession.set("UID", uid);
 		             System.out.println("-----------role_level-----"+role_level);
 		             //��Ϣ����
 		             //�����Լ�
		             /*PushService.subscribeMsg(usernameValue);
		             PushService.subscribeMsg(realname);
 		             if(role_level.equals("manager")){
 		            	 //����leader��
 		            	 PushService.subscribeMsg(PushService.NET_LEADER_GROUP);
 		             }else{
 		            	//����������
 		            	 PushService.subscribeMsg(PushService.NET_MASTER_GROUP);
 		             }
 		             //���Ŀͻ���AI
 		             PushService.subscribeMsg(PushService.MQTT_CLIENT_AI_ID);*/
 		    //         startGpsService();
 		             //��ת����  
 		          /* Intent intent = new Intent();
 		           intent.setAction("org.testxxx.helloworld.TaskCategoryActivity");
 		           startActivity(intent);*/
 		            Intent intent = new Intent(MainActivity.this,TaskCategoryActivity.class);
 		            //Intent intent = new Intent(MainActivity.this,GroupMessageActivity.class);
 		            MainActivity.this.startActivity(intent);  
 		             
 		             //finish();
 		         }else{
 		             Toast.makeText(MainActivity.this,"�û������������!", Toast.LENGTH_LONG).show();  
 		         }
 			}
     	};
     	commasynctask.execute(senddata);
    }  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello_world, menu);
        return true;
    }
    
    protected void onDestroy(){
        super.onDestroy();
        scd.unbindService();
    }
}
