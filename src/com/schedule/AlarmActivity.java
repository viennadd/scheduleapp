package com.schedule;



import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;

public class AlarmActivity extends Activity {
	private MediaPlayer mp;
	private Vibrator vibrator;
	
	private Button viewSchedule;
	private Button closeAlarm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		setTitle("日程提醒！");
		
		viewSchedule = (Button)findViewById(R.id.viewSchedule);
		closeAlarm = (Button)findViewById(R.id.closeAlarm);
		
		viewSchedule.setOnClickListener(new viewScheduleListener());
		closeAlarm.setOnClickListener(new closeAlarmListener());
	}
	
	class viewScheduleListener implements OnClickListener 
	{
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = getIntent();
			intent.setClass(getApplicationContext(), ViewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		// 添加此标识才可以从service开启Activity
			intent.putExtra("starter", "alarm");
			startActivity(intent);
			finish();
		}
	}
	
	class closeAlarmListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}

	// 在这里播放音乐和震动
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		// 播放声音
		mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);
		mp.setOnCompletionListener(new OnCompleteListener());
		mp.start();
		// 震动
		long pattern[] = new long[]{1000, 200};
		vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, 0);
		super.onStart();
	}

	class OnCompleteListener implements OnCompletionListener {

		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mp.start();
		}
	}
	
	// 在这里停止震动和音乐
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mp.release();
		vibrator.cancel();
		super.onStop();
	}
	
	
	
}
