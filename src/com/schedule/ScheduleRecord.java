package com.schedule;


import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;


public class ScheduleRecord {
	private String _id;
	private String subject;
	private String content;
	private Date startDate;
	private Date dueDate;
	private Date alarmDate;
	private Time alarmTime;
	private Date addDate;
	private Time addTime;
	
	public ScheduleRecord(Context context, String scheduleId) {
		DataItemOutput out = new DataItemOutput(context);
		Cursor cursor = out.select("schedule", null, "_id=?", new String[]{scheduleId}, null, null, null);
		
		if (cursor.moveToFirst()) {
			_id = cursor.getString(cursor.getColumnIndex("_id"));
			subject = cursor.getString(cursor.getColumnIndex("subject"));
			content = cursor.getString(cursor.getColumnIndex("content"));
			startDate = DateInit(cursor.getString(cursor.getColumnIndex("startDate")));
			dueDate = DateInit(cursor.getString(cursor.getColumnIndex("dueDate")));
			alarmDate = DateInit(cursor.getString(cursor.getColumnIndex("alarmDate")));
			addDate = DateInit(cursor.getString(cursor.getColumnIndex("addDate")));
			alarmTime = TimeInit(cursor.getString(cursor.getColumnIndex("alarmTime")));
			addTime = TimeInit(cursor.getString(cursor.getColumnIndex("addTime")));
		}
		cursor.close();
		out.close();
	}
	
	// 传入数据库存时间的格式，yyyy年mm月dd日
	// 返回初始化完毕的Date
	private Date DateInit(String DbDate)
	{
		if (DbDate.equalsIgnoreCase("no alarm")) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			return new Date(c);
		}
		int monthIndexEnd = DbDate.indexOf("月");
		int dayIndexStart = monthIndexEnd + 1;
		int dayIndexEnd = DbDate.length() - 1;
		
		int year = Integer.parseInt(DbDate.substring(0, 4));
		int month = Integer.parseInt(DbDate.substring(5, monthIndexEnd)) - 1;
		int day = Integer.parseInt(DbDate.substring(dayIndexStart, dayIndexEnd));
		
		Date date = new Date(year, month, day);
		return date;
	}
	
	// 传入数据库存时间的格式，hh:mm
	// 返回初始化完毕的Time
	private Time TimeInit(String DbTime)
	{
		if (DbTime.equalsIgnoreCase("no alarm")) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			return new Time(c);
		}
		int hour = Integer.parseInt(DbTime.substring(0, 2));
		int minute = Integer.parseInt(DbTime.substring(3, 5));

		Time time = new Time(hour, minute);

		return time;
	}

	public String get_id() {
		return _id;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Date getAlarmDate() {
		return alarmDate;
	}

	public Time getAlarmTime() {
		return alarmTime;
	}

	public Date getAddDate() {
		return addDate;
	}

	public Time getAddTime() {
		return addTime;
	}
}
