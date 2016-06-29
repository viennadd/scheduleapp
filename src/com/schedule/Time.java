package com.schedule;

import java.util.Calendar;

import android.widget.TimePicker;

public class Time {
	private int hour;
	private int minute;
	
	public Time(int hour, int minute)
	{
		this.hour = hour;
		this.minute = minute;
	}
	
	public Time(TimePicker time)
	{
		hour = time.getCurrentHour();
		minute = time.getCurrentMinute();
	}
	public Time(Calendar c)
	{
		this.hour = c.get(Calendar.HOUR_OF_DAY);
		this.minute = c.get(Calendar.MINUTE);
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}
	
	
}
