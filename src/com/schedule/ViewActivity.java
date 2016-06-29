package com.schedule;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewActivity extends Activity {

	private String scheduleId;
	private String starter;
	
	private TextView viewTitle;
	private TextView viewContent;
	private TextView viewStartDate;
	private TextView viewDueDate;
	private TextView viewAlarmDate;
	private TextView viewAlarmTime;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		
		Intent intent = getIntent();
		scheduleId = intent.getStringExtra("scheduleId");
		starter = intent.getStringExtra("starter");
		
		Log.d("debug", "schdule id pass in view = " + scheduleId);
		
		bindViewsWithVars();
		setScheduleView();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.viewmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent().putExtra("scheduleId", scheduleId);
		
		switch (item.getItemId()) {
		case R.id.viewmenu_edit:
			goActivity(intent, EditActivity.class);
			finish();
			return true;
		case R.id.viewmenu_delete:
			DataItemInput in = new DataItemInput(getApplicationContext());
			in.delete("_id=?", new String[]{scheduleId});
			in.close();
			finish();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}
	
	

	private void setScheduleView()
	{
		DataItemOutput out = new DataItemOutput(getApplicationContext());
		
		Cursor cursor = out.select("schedule", null, "_id=?", new String[]{scheduleId}, null, null, null);
		
		if (cursor.moveToFirst()) {
			viewTitle.setText(cursor.getString(cursor.getColumnIndex("subject")));
			viewContent.setText(cursor.getString(cursor.getColumnIndex("content")));
			viewStartDate.setText(cursor.getString(cursor.getColumnIndex("startDate")));
			viewDueDate.setText(cursor.getString(cursor.getColumnIndex("dueDate")));
			viewAlarmDate.setText(cursor.getString(cursor.getColumnIndex("alarmDate")));
			viewAlarmTime.setText(cursor.getString(cursor.getColumnIndex("alarmTime")));
		}
		
		out.close();
		cursor.close();
	}
	
	private void bindViewsWithVars()
	{
		setTitle(R.string.mainitem_view);
		viewTitle = (TextView)findViewById(R.id.viewTitle);
		viewContent = (TextView)findViewById(R.id.viewContent);
		viewStartDate = (TextView)findViewById(R.id.viewStartDate);
		viewDueDate = (TextView)findViewById(R.id.viewDueDate);
		viewAlarmDate = (TextView)findViewById(R.id.viewAlarmDate);
		viewAlarmTime = (TextView)findViewById(R.id.viewAlarmTime);
	}

	private void goActivity(Intent intent, Class<?> cls)
	{
		intent.setClass(this, cls);
		this.startActivity(intent);
	}
}
