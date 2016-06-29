package com.schedule;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditActivity extends Activity {
	private String scheduleId;
	
	private DatePicker startDate;
	private DatePicker dueDate;
	private DatePicker AlarmDate;
	private DatePicker nowDate;
	private TimePicker nowTime;
	private TimePicker AlarmTime;
	
	private EditText editTitle;
	private EditText editContent;
	private TextView labAlarm;

	private Button confirmEdit;
	private CheckBox isUseAlarm;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		
		Intent intent = getIntent();
		scheduleId = intent.getStringExtra("scheduleId");
		
		Log.d("debug", "schdule id pass in = " + scheduleId);
		bindViewsWithVars();
		
		initViewsData();
		
		confirmEdit.setOnClickListener(new confirmEditListener());
		isUseAlarm.setOnCheckedChangeListener(new isUseAlrmOnCheckedChangeListener());
	}
	
	class isUseAlrmOnCheckedChangeListener implements OnCheckedChangeListener
	{
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
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
	
	class confirmEditListener implements OnClickListener {

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
			ContentValues values = new ContentValues();
			DataItemInput in = new DataItemInput(getApplicationContext());
			
			nowDate = new DatePicker(getApplicationContext());
			nowTime = new TimePicker(getApplicationContext());
			String strNowDate = timeHelp.DateToDBFormat(nowDate);
			String strNowTime = timeHelp.TimeToDBFromat(nowTime);
			
			values.put("subject", editTitle.getText().toString());
			values.put("content", editContent.getText().toString());
			values.put("startDate", timeHelp.DateToDBFormat(startDate));
			values.put("dueDate", timeHelp.DateToDBFormat(dueDate));
			values.put("alarmDate", strAlarmDate);
			values.put("alarmTime", strAlarmTime);
			values.put("addDate", strNowDate);
			values.put("addTime", strNowTime);
			if (Global.isExist(EditActivity.this, values)) {
				showToast("条目重复，请重新检查");
				return;
			}
			
			in.update(values, "_id=?", new String[]{scheduleId});
			in.close();
			
			// 如果勾选闹钟则设置AlarmManager
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			Alarm alarm = new Alarm(am, AlarmDate, AlarmTime);
			
			Intent intent = new Intent();
			// 把scheduleId传入，方便闹钟触发时查询日程条目
			intent.putExtra("scheduleId", scheduleId);
			intent.setClass(EditActivity.this, AlarmService.class);
			PendingIntent peIntent = PendingIntent.getService(EditActivity.this, Integer.parseInt(scheduleId), intent, 0);
			if (isUseAlarm.isChecked()) {
				alarm.cancel(peIntent);
				alarm.set(peIntent);
			} else {
				// 没有勾选则取消闹钟
				alarm.cancel(peIntent);
			}
			
			intent.setClass(EditActivity.this, ViewActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	


	private void showToast(CharSequence text)
	{
		Log.d("debug", text.toString());
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	
	private void initViewsData() 
	{
		ScheduleRecord record = new ScheduleRecord(getApplicationContext(), scheduleId);
		
		Date date = record.getStartDate();
		startDate.init(date.getYear(), date.getMonth(), date.getDay(), null);
		date = record.getDueDate();
		dueDate.init(date.getYear(), date.getMonth(), date.getDay(), null);
		

		date = record.getAlarmDate();
		AlarmDate.init(date.getYear(), date.getMonth(), date.getDay(), null);
		
		Time time = record.getAlarmTime();
		AlarmTime.setCurrentHour(time.getHour());
		AlarmTime.setCurrentMinute(time.getMinute());
			
		editTitle.setText(record.getSubject());
		editContent.setText(record.getContent());
	}
	
	private void bindViewsWithVars()
	{
		setTitle(R.string.mainitem_edit);
		startDate = (DatePicker)findViewById(R.id.editStartDate);
		dueDate = (DatePicker)findViewById(R.id.editDueDate);
		AlarmDate = (DatePicker)findViewById(R.id.editAlarmDate);
		AlarmTime = (TimePicker)findViewById(R.id.editAlarmTime);
		editTitle = (EditText)findViewById(R.id.editTitle);
		editContent = (EditText)findViewById(R.id.editContent);
		
		confirmEdit = (Button)findViewById(R.id.confirmEdit);
		isUseAlarm = (CheckBox)findViewById(R.id.editIsUseAlarm);
		
		nowDate = new DatePicker(getApplicationContext());
		nowTime = new TimePicker(getApplicationContext());
		
		// 默认false
		AlarmTime.setIs24HourView(true);
		nowTime.setIs24HourView(true);
		
		labAlarm = (TextView)findViewById(R.id.editLabAlarm);
		
		AlarmDate.setVisibility(View.GONE);
		AlarmTime.setVisibility(View.GONE);
		labAlarm.setVisibility(View.GONE);
	}

}
