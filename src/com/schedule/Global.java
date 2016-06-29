package com.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Global {
	public static final int CONTENT_LENGTH = 140;
	public static final int TITLE_LENGTH = 32;
	public static final String columns[] = new String[]{"subject", 
														"content", 
														"startDate", 
														"dueDate", 
														"alarmDate", 
														"alarmTime",
														"addDate",
														"addTime"};
	
	public static boolean isExist(Context context, String values[])
	{
		DataItemOutput out = new DataItemOutput(context);
		String selection = "";
		int selection_length = columns.length;
		for (int i = 0;i < selection_length - 1 - 2;++i) { // 最后两个关于最后修改时间的不需要，所以-2
			selection += columns[i] + " = ? and ";
		}
		selection += columns[selection_length - 1 - 2] + " = ?";
		String values_local[] = new String[selection_length - 2];
		for (int i = 0;i < values_local.length;++i) {
			values_local[i] = values[i];
		}
		Log.d("selection", selection);
		Cursor cursor = out.select("schedule", columns, selection, values_local, null, null, null);
		if (cursor.getCount() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isExist(Context context, ContentValues contentValues)
	{
		String values[] = new String[columns.length];
		for (int i = 0;i < values.length;++i) {
			values[i] = contentValues.getAsString(columns[i]);
		}
		
		return isExist(context, values);
	}
}
