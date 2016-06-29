package com.schedule;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Alarm {
	private AlarmManager am;
	private Calendar calendar;
	private Intent intent;
	private PendingIntent peIntent;
	private Date date;
	private Time time;
	
	public Alarm(AlarmManager am, Date date, Time time)
	{
		init(am, date, time);
	}
	
	public Alarm(AlarmManager am, DatePicker datePicker, TimePicker timePicker)
	{
		Date date = new Date(datePicker);
		Time time = new Time(timePicker);
		init(am, date, time);
	}
	
	private void init(AlarmManager am, Date date, Time time)
	{
		this.am = am;
		this.date = date;
		this.time = time;
		this.calendar = Calendar.getInstance();
		setCalendar();
	}
	
	public void setCalendar(Date date, Time time)
	{
		Log.d("debug", String.format("%d %d %d    %d:%d", date.getYear(),date.getMonth(), date.getDay(), time.getHour(), time.getMinute()));
		// 把日历设置成闹钟时间
		calendar.set(Calendar.YEAR, date.getYear());
		calendar.set(Calendar.MONTH, date.getMonth());	// 月份注意0开始
		calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
		
		calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
		calendar.set(Calendar.MINUTE, time.getMinute());
		calendar.set(Calendar.SECOND, 0);
		
		Log.d("debug", "calendar : " + calendar.toString());
	}
	
	// 设置闹钟事件，事件为传入的PendingIntent
	public void set(PendingIntent pi)
	{
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
	}
	
	public void cancel(PendingIntent pi)
	{
		am.cancel(pi);
	}
	
	public void set()
	{
		set(this.peIntent);
	}
	
	
	
	public void setCalendar()
	{
		setCalendar(this.date, this.time);
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public PendingIntent getPeIntent() {
		return peIntent;
	}

	public void setPeIntent(PendingIntent peIntent) {
		this.peIntent = peIntent;
	}
	
	
}
