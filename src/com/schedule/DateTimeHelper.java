package com.schedule;

import android.widget.DatePicker;
import android.widget.TimePicker;

public class DateTimeHelper {

	public String DateToDBFormat(DatePicker date) {
		
		return String.format("%d年%02d月%02d日", 
						date.getYear(),
						date.getMonth() + 1, // 0 based
						date.getDayOfMonth());
	}
	
	public String DateToDBFormat(Date date) {
		
		return String.format("%d年%02d月%02d日", 
						date.getYear(),
						date.getMonth() + 1, // 0 based
						date.getDay());
	}
	
	public String TimeToDBFromat(TimePicker time) {
		
		return String.format("%02d:%02d", 
						time.getCurrentHour(),
						time.getCurrentMinute());
	}
	
	public String TimeToDBFromat(Time time) {
		
		return String.format("%02d:%02d", 
						time.getHour(),
						time.getMinute());
	}
	
	/*
	 * 第一个时间是否小于第二个时间
	 */
	public boolean timeCmp(DatePicker A, DatePicker B)
	{
		
		if (A.getYear() < B.getYear()) {
			return true;
		} else if (A.getYear() > B.getYear()) {
			return false;
		} else {
			// 年份相等
			if (A.getMonth() < B.getMonth()) {
				return true;
			} else if (A.getMonth() > B.getMonth()) {
				return false;
			} else {
				// 月份相等
				if (A.getDayOfMonth() < B.getDayOfMonth()) {
					return true;
				} else {
					return false;
				}
			}
		}
	} // end of timeCmp()
	
	
}
