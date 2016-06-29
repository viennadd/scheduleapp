package com.schedule;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {


	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		// 闹钟触发后各种动作
		Log.d("debug", "闹钟service run " + flags + " " + startId);
		DataItemInput in = new DataItemInput(getApplicationContext());

		intent.setClass(getApplicationContext(), AlarmActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		// 添加此标识才可以从service开启Activity
		intent.putExtra("starter", "service");
		startActivity(intent);
		
		// 响闹后设成无闹钟
		Log.d("debug", "in service, the id in intent : " + intent.getStringExtra("scheduleId"));
		in.update(	new String[]{"alarmDate", "alarmTime"}, 
					new String[]{"no alarm", "no alarm"}, 
					"_id=?",
					new String[]{intent.getStringExtra("scheduleId")});
		in.close();
		
		return super.onStartCommand(intent, flags, startId);
	}
}
