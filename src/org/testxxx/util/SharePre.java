package org.testxxx.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePre extends Activity {
	private SharedPreferences sp; 
	//private static String sharefile;
	
	public SharePre(SharedPreferences shared){
		sp = shared;
	}
	
	//����int����
	public void setInt(String key, int value){
		Editor editor = sp.edit(); 
		editor.putInt(key, value);
		editor.commit(); 
	}
	
	//����string����
	public void setString(String key, String value){
		Editor editor = sp.edit(); 
		editor.putString(key, value);
		editor.commit(); 
	}
	
	//����boolean����
	public void setBoolean(String key, Boolean value){
		Editor editor = sp.edit(); 
		editor.putBoolean(key, value);
		editor.commit(); 
	}
	
	//��������
	public void setStringSet(){
		
	}
	
	//��ȡintֵ
	public int getInt(String key, int defValue){
		int val = sp.getInt(key, defValue);
		return val;
	}
	
	//��ȡstringֵ
	public String getString(String key, String defValue){
		String val = sp.getString(key, defValue);
		return val;
	}
	
	//��ȡBooleanֵ
	public Boolean getBoolean(String key, Boolean defValue){
		Boolean val = sp.getBoolean(key, defValue);
		return val;
	}
	
	
	
	
}
