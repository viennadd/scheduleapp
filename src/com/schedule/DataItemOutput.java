// 封装数据库的输出操作

package com.schedule;


import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataItemOutput {
	
	private ArrayList<HashMap<String, String>> ItemList;
	private Cursor cursor = null;
	private SQLiteDatabase db;

	public DataItemOutput(Context context) {
		db = new DBHelper(context).getReadableDatabase();
	}

	public ArrayList<HashMap<String, String>> getItem(String [] columns)
	{
		ItemList = new ArrayList<HashMap<String,String>>();
		
		while (cursor.moveToNext()) {
			HashMap<String, String> item = new HashMap<String, String>();
			
			for (int i = 0;i < columns.length;++i) {
				// item.put(columns[i], new String(cursor.getBlob(cursor.getColumnIndex(columns[i])), "GBK"));
				item.put(columns[i], cursor.getString(cursor.getColumnIndex(columns[i])));
			}
	    	ItemList.add(item);
		}
		
		return ItemList;
	}

	
	public Cursor select(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) 
	{
		return cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	public String getLastInsertId(String table)
	{
		Cursor cursor = db.query(table, new String[]{"_id"}, null, null, null, null, "_id desc");
		if (cursor.moveToFirst()) {
			return cursor.getString(cursor.getColumnIndex("_id"));
		}	
		return null;
	}
	
	public void close()
	{
		db.close();
	}


}


