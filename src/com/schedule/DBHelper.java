// 需要这里继承SQLiteOpenHelper才可以使用

package com.schedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DBHelper(Context context) {
		super(context, "schedule.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("debug", "datebase create");
		
		// 表需要有主键，否则query时会有问题
		String sql = new String("create table schedule(_id integer primary key autoincrement, type_id integer, subject varchar(64), content varchar(512), startDate char(8), dueDate char(8), alarmDate char(8), alarmTime char(4), addDate char(8), addTime char(4))");
		db.execSQL(sql);
//		sql = new String("create table type(_id integer primary key autoincrement, typename varchar(32))");
//		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
