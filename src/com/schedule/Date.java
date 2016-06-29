package com.schedule;

import java.util.Calendar;

import android.widget.DatePicker;

public class Date {
	private int year;
	private int month;
	private int day;
	
	public Date(int year, int month, int day)
	{
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	public Date(DatePicker datePicker)
	{
		this.year = datePicker.getYear();
		this.month = datePicker.getMonth();
		this.day = datePicker.getDayOfMonth();
	}
	
	public Date(Calendar c)
	{
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
		this.day = c.get(Calendar.DAY_OF_MONTH);
	}
	
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	
}
