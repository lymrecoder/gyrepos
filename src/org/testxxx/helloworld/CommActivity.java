package org.testxxx.helloworld;

import org.testxxx.itfc.CommAsyncTask;
import org.testxxx.itfc.SrvrCommDelegate;

import android.app.Activity;
import android.os.Bundle;

/**
 * �жԷ�������������������activity��Ĭ�ϰ�SrvrComm
 * @author yufulou
 *
 */
public class CommActivity extends Activity {

	protected CommAsyncTask commasynctask;
	protected SrvrCommDelegate scd;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scd = new SrvrCommDelegate(this);
		scd.bindService();
	}
    
    protected void onDestroy(){
        super.onDestroy();
        scd.unbindService();
    }
}
