package com.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		// 重启后设回闹钟，过滤已经响过的闹钟
		// 可以在响闹的service里设置过滤
		DataItemOutput out = new DataItemOutput(context);
		Cursor cursor = out.select("schedule", null, null, null, null, null, null);
		ScheduleRecord record;
		while (cursor.moveToNext()) {
			if (cursor.getString(cursor.getColumnIndex("alarmDate")).equalsIgnoreCase("no alarm"))
				continue;
			
			// 循环设置有闹钟时间的条目
			record = new ScheduleRecord(context, 
					cursor.getString(cursor.getColumnIndex("_id")));
			AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Alarm alarm = new Alarm(am, record.getAlarmDate(), record.getAlarmTime());
			
			Intent alarmIntent = new Intent();
			// 把scheduleId传入，方便闹钟触发时查询日程条目
			alarmIntent.putExtra("scheduleId", record.get_id());
			Log.d("debug", "boot reset alarm id : " + record.get_id());
			alarmIntent.setClass(context, AlarmService.class);
			PendingIntent peIntent = PendingIntent.getService(context, Integer.parseInt(record.get_id()), alarmIntent, 0);
			alarm.set(peIntent);
		}
		out.close();
		cursor.close();
	}
}
