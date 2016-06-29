package com.schedule;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddActivity extends Activity {
	
	private DatePicker startDate;
	private DatePicker dueDate;
	private DatePicker AlarmDate;
	private DatePicker nowDate;
	private TimePicker nowTime;
	private TimePicker AlarmTime;
	
	private EditText editTitle;
	private EditText editContent;
	private TextView labAlarm;
	private Button confirmAdd;
	private CheckBox isUseAlarm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		
		bindViewsWithVars();
		
		isUseAlarm.setOnCheckedChangeListener(new isUseAlrmOnCheckedChangeListener());
		
		confirmAdd.setOnClickListener(new confirmAddOnClickListener());
	}

	
	class isUseAlrmOnCheckedChangeListener implements OnCheckedChangeListener
	{
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			// 是否显示闹钟选项
			if (isChecked) {
				AlarmDate.setVisibility(View.VISIBLE);
				AlarmTime.setVisibility(View.VISIBLE);
				labAlarm.setVisibility(View.VISIBLE);
			} else {
				AlarmDate.setVisibility(View.GONE);
				AlarmTime.setVisibility(View.GONE);
				labAlarm.setVisibility(View.GONE);
			}
		}	
	}

	class confirmAddOnClickListener implements OnClickListener
	{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			// 时间值各种检查
			DateTimeHelper timeHelp = new DateTimeHelper();
			
			// 开始日期是否在今日之前
			if (timeHelp.timeCmp(startDate, nowDate)) {
				showToast("开始日期在今日之前，请重新检查");
				return;
			}
			// 到期日期是否在开始日期之前
			if (timeHelp.timeCmp(dueDate, startDate)) {
				showToast("到期日期在开始日期之前，请重新检查");
				return;
			}
			
			
			String strAlarmDate = timeHelp.DateToDBFormat(AlarmDate);
			String strAlarmTime = timeHelp.TimeToDBFromat(AlarmTime);
			if (isUseAlarm.isChecked()) {
				
				// 闹钟日期是否在到期日和今日之内
				if (timeHelp.timeCmp(dueDate, AlarmDate) || 
					timeHelp.timeCmp(AlarmDate, nowDate)) 
				{
					showToast("闹钟日期不在到期日和现时之内请重新检查");
					return;
				}
			} else {
				// 没有选择闹钟功能
				// 加入标识，方便在重启后重设闹钟
				strAlarmTime = "no alarm";
				strAlarmDate = "no alarm";
			}

			// 检查各种内容是否输入
			if (editTitle.getText().toString().isEmpty()) {
				showToast("标题不能为空");
				return;
			}
			
			// 标题和内容长度检测
			if (editTitle.getText().toString().length() > Global.TITLE_LENGTH) {
				showToast("标题太长，需要小于32个字符");
				return;
			}
			if (editContent.getText().toString().length() > Global.CONTENT_LENGTH) {
				showToast("标题太长，需要小于140个字符");
				return;
			}
			
			// 入数据库			
			DataItemInput in = new DataItemInput(getApplicationContext());
			
			String values[] = new String[]{ editTitle.getText().toString(),
											editContent.getText().toString(),
											timeHelp.DateToDBFormat(startDate),
											timeHelp.DateToDBFormat(dueDate),
											strAlarmDate,
											strAlarmTime,
											timeHelp.DateToDBFormat(nowDate),
											timeHelp.TimeToDBFromat(nowTime)};
			
			if (Global.isExist(AddActivity.this, values)) {
				showToast("条目重复，请重新检查");
				return;
			}
			in.DBInsert("schedule", null, Global.columns, values);
			in.close();
			
			// 如果勾选闹钟则设置AlarmManager
			Intent intent = new Intent();
			/* 为闹钟获取scheduleId */
			DataItemOutput out = new DataItemOutput(getApplicationContext());
			String scheduleId = out.getLastInsertId("schedule");
			Log.d("debug", "last insert : " + scheduleId);
			out.close();
			
			// 把scheduleId传入，方便闹钟触发时查询日程条目
			intent.putExtra("scheduleId", scheduleId);
			if (isUseAlarm.isChecked()) {	
				AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
				Alarm alarm = new Alarm(am, AlarmDate, AlarmTime);
				
				intent.setClass(AddActivity.this, AlarmService.class);
				PendingIntent peIntent = PendingIntent.getService(AddActivity.this, Integer.parseInt(scheduleId), intent, 0);
				alarm.set(peIntent);
			}
			
			intent.setClass(AddActivity.this, ViewActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	private void showToast(CharSequence text)
	{
		Log.d("debug", text.toString());
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	
	private void bindViewsWithVars()
	{
		setTitle(R.string.mainitem_add);
		startDate = (DatePicker)findViewById(R.id.startDate);
		dueDate = (DatePicker)findViewById(R.id.dueDate);
		AlarmDate = (DatePicker)findViewById(R.id.alarmDate);
		AlarmTime = (TimePicker)findViewById(R.id.alarmTime);
		
		nowDate = new DatePicker(getApplicationContext());
		nowTime = new TimePicker(getApplicationContext());
		
		// 默认false
		AlarmTime.setIs24HourView(true);
		nowTime.setIs24HourView(true);
		
		confirmAdd = (Button)findViewById(R.id.confirmAdd);
		
		editTitle = (EditText)findViewById(R.id.titleEdit);
		editContent = (EditText)findViewById(R.id.contentEdit);
		labAlarm = (TextView)findViewById(R.id.labAlarm);
		isUseAlarm = (CheckBox)findViewById(R.id.isUseAlarm);
		
		AlarmDate.setVisibility(View.GONE);
		AlarmTime.setVisibility(View.GONE);
		labAlarm.setVisibility(View.GONE);
	}
}
