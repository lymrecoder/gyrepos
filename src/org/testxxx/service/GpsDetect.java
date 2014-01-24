package org.testxxx.service;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import org.testxxx.itfc.CommAsyncTask;
import org.testxxx.itfc.CommIBinder;
import org.testxxx.itfc.GpsIBinder;
import org.testxxx.itfc.SrvrCommDelegate;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class GpsDetect extends Service {

	final static int GPSINTVAL = 5;
	
	private GpsBinder gps_binder = new GpsBinder();
	private CommAsyncTask commasynctask;
	private SrvrCommDelegate scd;
	
	private String lastLongtitude;
	private String lastLatitude;
	private String lastRecvtime;
	private String lastRadius;
	
	private LocationClient mLocationClient;
	private GpsLocationListener loc_lstn = new GpsLocationListener();
	
	public class GpsLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			
			lastLatitude = String.valueOf(location.getLatitude());
			lastLongtitude = String.valueOf(location.getLongitude());
			lastRecvtime = location.getTime();
			lastRadius = String.valueOf(location.getRadius());
			
			Hashtable<String, Object> senddata = new Hashtable<String, Object>();
			CommIBinder binder = scd.getBinder();
	     	senddata.put("action", "5");
	    	senddata.put("method", "1");
	    	senddata.put("latitude", lastLatitude);
	    	senddata.put("longtitude", lastLongtitude);
	    	senddata.put("recvtime", lastRecvtime);
	    	senddata.put("radius", lastRadius);
	    	commasynctask = new CommAsyncTask(binder){
	    		
	    		@Override
	    		public void onPostExecute(String ret){
	    			Hashtable<String, Object> b = decodeRecvdata(ret);
	    			onRecvData(b);
	    		}
	    		
	    		@Override
	    		public void execute(Hashtable<String, Object> sendData){
	    			JSONObject obj = new JSONObject(sendData);
	    			String str = obj.toString();
	    			execute(str);
	    		}
	    		
				@SuppressWarnings("unchecked")
				@Override
				public void onRecvData(Hashtable<String, Object> recvData) {
			    	System.out.println("����gps�ɹ�"+recvData.toString());
				}
	    	};
	    	commasynctask.execute(senddata);
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
		/*	StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			} 
			if(poiLocation.hasPoi()){
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}else{				
				sb.append("noPoi information");
			}*/
		}
		
	}

	
	public class GpsBinder extends Binder implements GpsIBinder{
		@Override
		public String fetchLocation(String msg) throws InterruptedException {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private void initLocationClient(){
		mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
	    LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(true);
	    option.setAddrType("all");//���صĶ�λ���������ַ��Ϣ
	    option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
	    option.setScanSpan(GPSINTVAL * 1000);//���÷���λ����ļ��ʱ��Ϊ5000ms
	    option.disableCache(true);//��ֹ���û��涨λ
	    option.setPoiNumber(5);	//��෵��POI����	
	    option.setPoiDistance(1000); //poi��ѯ����		
	    option.setPoiExtraInfo(true); //�Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ		
	    mLocationClient.setLocOption(option);
	    mLocationClient.registerLocationListener(loc_lstn);    //ע���������if (mLocClient != null && mLocClient.isStarted())
	    mLocationClient.start();
	    
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		initLocationClient();
		
		scd = new SrvrCommDelegate(this);
		scd.bindService();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
        scd.unbindService();
        mLocationClient.stop();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return gps_binder;
	}
}
