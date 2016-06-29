package com.schedule;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ScheduleActivity extends Activity {


	private ListView listView;
	private CalendarView calendarView;
	private ArrayList<HashMap<String, String>> scheduleList;
	private SimpleAdapter adapter;
	
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// 显示列表内容
		refreshListView();
	}

	/** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// 控件初始化
		listView = (ListView)findViewById(R.id.lsItem);
		calendarView = (CalendarView)findViewById(R.id.calendar);
		calendarView.setVisibility(View.GONE);
		
		// 显示列表内容
		refreshListView();
		
		// 绑定各种事件
		registerForContextMenu(findViewById(R.id.lsItem));

		listView.setOnItemClickListener(new OnScheduleClickListener());
	}
    
    class OnScheduleClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// 列表条目被点击时，默认查看此条目内容
			Intent intent = new Intent();
	        intent.putExtra("scheduleId", scheduleList.get(arg2).get("_id"));
	        intent.putExtra("starter", "main");
	        Log.d("debug", String.format("第%d个item，view", arg2));
			goActivity(intent, ViewActivity.class);
		}
    }
    

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// 获取context menu position
		AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Intent intent = new Intent();
        String scheduleId = scheduleList.get(menuInfo.position).get("_id");
        intent.putExtra("scheduleId", scheduleId);
		
		switch (item.getItemId()) {
		case R.id.mainitem_view:
			Log.d("debug", String.format("第%d个item，view", menuInfo.position));
			goActivity(intent, ViewActivity.class);
			return true;
		case R.id.mainitem_edit:
			Log.d("debug", String.format("第%d个item，edit", menuInfo.position));
			goActivity(intent, EditActivity.class);
			return true;
		case R.id.mainitem_delete:
			Log.d("debug", String.format("第%d个item，delete", menuInfo.position));
			DataItemInput in = new DataItemInput(getApplicationContext());
			in.delete("_id=?", new String[]{scheduleId});
			in.close();
			refreshListView();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.maincontextmenu, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.mainmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mainitem_add:
			goActivity(new Intent(), AddActivity.class);
			return true;
			
		case R.id.clearallschedule:
			clearAllSchedule(ScheduleActivity.this);
			return true;
			
		case R.id.mainitem_switchmode:
			switchCanlenderMode();
			return true;
			
		case R.id.recommand:
			Uri uri = Uri.parse("smsto:");
			Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, uri);
			intent.putExtra("sms_body", "亲爱的朋友，我正在使用一个日程管理软件，很不错哦，你也来使用一下吧。");
			startActivity(intent);
			return true;
			
		case R.id.about:
			showAboutDialog(ScheduleActivity.this);
			return true;
			
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}
	
	private void switchCanlenderMode() {
		// TODO Auto-generated method stub
		if (calendarView.getVisibility() == View.GONE) {
			calendarView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			calendarView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}

	private void clearAllSchedule(Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.clear_all_schedule);
		builder.setMessage(R.string.clear_message);
		builder.setPositiveButton("确定", new OnClickListener() {
			private DataItemInput in = new DataItemInput(ScheduleActivity.this);
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				in.delete(null, null);
				in.close();
				refreshListView();
			}
		});
		
		builder.setNegativeButton("返回", null);
		builder.show();
	}
	
	private void showAboutDialog(Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("关于");
		builder.setMessage(R.string.aboutMessage);
		builder.setPositiveButton("OK", null);
		builder.show();
	}
	
	private void goActivity(Intent intent, Class<?> cls)
	{
		intent.setClass(this, cls);
		this.startActivity(intent);
	}

    private void refreshListView()
    {
    	scheduleList = getItems();
    			
		adapter = new SimpleAdapter(this, scheduleList, 
				R.layout.item, 
				new String[]{"subject", "addDate", "addTime"}, 
				new int[]{R.id.subject, R.id.addDate, R.id.addTime});
		
		listView.setAdapter(adapter);
    }
    
    private ArrayList<HashMap<String, String>> getItems()
    {
    	DataItemOutput out = new DataItemOutput(getApplicationContext());
    	
    	// select * from schedule order by addDate, addTime
    	out.select("schedule", null, null, null, null, null, "addDate desc, addTime desc");
    	
    	return out.getItem(new String[]{"_id", "subject", "addDate", "addTime"});
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 模拟home键让返回键不关闭app
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 注意
			intent.addCategory(Intent.CATEGORY_HOME);
			this.startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}