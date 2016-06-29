// 封装数据库的插入操作

package com.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataItemInput {
	private SQLiteDatabase db;
	private ContentValues contentValues;

	public DataItemInput(Context context) {
		contentValues = new ContentValues();
		db = new DBHelper(context).getWritableDatabase();
	}
	
	public long DBInsert(String table, String nullColumnHack, String columns[], Object values[]) {
		
		for (int i = 0;i < columns.length;++i) {
			contentValues.put(columns[i], (String) values[i]);
		}
		
		return db.insert(table, nullColumnHack, contentValues);
	}
	
	public int update(ContentValues values, String whereClause, String[] whereArgs)
	{
		return db.update("schedule", values, whereClause, whereArgs);
	}
	
	public int update( String columns[], Object values[], String whereClause, String[] whereArgs)
	{
		for (int i = 0;i < columns.length;++i) {
			contentValues.put(columns[i], (String) values[i]);
		}
		
		return db.update("schedule", contentValues, whereClause, whereArgs);
	}
	
	public int delete(String whereClause, String[] whereArgs)
	{
		return db.delete("schedule", whereClause, whereArgs);
	}
	
	public void close()
	{
		db.close();
	}

	
}
