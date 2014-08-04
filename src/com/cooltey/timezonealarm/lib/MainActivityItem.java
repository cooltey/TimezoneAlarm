package com.cooltey.timezonealarm.lib;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cooltey.timezonealarm.R;
import com.cooltey.timezonealarm.TimeZoneActivity;


public class MainActivityItem{

	private static Context      mContext;
	private Cursor       mCursor;
	
	private EditText     titleView;
	private ToggleButton switchView;
	private TextView     alarmTimeView;
	private static TextView     alarmTimeZoneView;
	
	private static ColumnData setColumnData = new ColumnData();
		
	public MainActivityItem(Context context, Cursor cursor){
		mContext = context;
		mCursor	 = cursor;
	}
	
	public View getItemView(){
		View getView = null;
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		getView = inflater.inflate(R.layout.item_main, null);	
		
		titleView          = (EditText) getView.findViewById(R.id.title);
		switchView         = (ToggleButton) getView.findViewById(R.id.switcher);
		alarmTimeView      = (TextView) getView.findViewById(R.id.alarm_time);
		alarmTimeZoneView  = (TextView) getView.findViewById(R.id.alarm_timezone);
		
		// set View data
		setItemData();
		
		// set Action
		setViewAction();
		
		return getView;
	}
	
	public void setItemData(){
		if(mCursor != null){
			setColumnData.getId 			= mCursor.getString(0);
			setColumnData.getTitle 			= mCursor.getString(1);
			setColumnData.getSwitcher 		= Boolean.parseBoolean(mCursor.getString(2));
			setColumnData.getAlarmTime 		= mCursor.getString(3);
			setColumnData.getAlarmTimeZone	= mCursor.getString(4);
			
			// set value

			titleView.setText(setColumnData.getTitle);
			switchView.setChecked(setColumnData.getSwitcher);
			alarmTimeView.setText(setColumnData.getAlarmTime);
			alarmTimeZoneView.setText(setColumnData.getAlarmTimeZone);
		}
	}
	
	private void setViewAction(){
		
		titleView.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				setColumnData.getTitle = titleView.getEditableText().toString();
				saveIntoDB();
			}
			
		});
		
		switchView.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				// save into database
				setColumnData.getSwitcher = switchView.isChecked();
				saveIntoDB();
			}
			
		});
		
		alarmTimeView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c        = Calendar.getInstance(); 
				final TextView tv = (TextView) v;
				int setHour 	  = c.get(Calendar.HOUR_OF_DAY);
				int setMinute	  =	c.get(Calendar.MINUTE);
				
				String getCurrentPick = tv.getText().toString();
				if(getCurrentPick.length() > 0){
					String[] currentTime = getCurrentPick.split(":");
					setHour 	= Integer.parseInt(currentTime[0]);
					setMinute 	= Integer.parseInt(currentTime[1]);
				}
				
		        // TODO Auto-generated method stub  
		        new TimePickerDialog(mContext,  
		        		new OnTimeSetListener() {  
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						 // TODO Auto-generated method stub
						 String newHours = "" + hourOfDay;
						 if(hourOfDay < 10){
							 newHours = "0" + hourOfDay;
						 }
						 
						 
						 String newMins = "" + minute;
						 if(minute < 10){
							 newMins = "0" + minute;
						 }
						 tv.setText(newHours + ":" + newMins);
						 // set data
						 setColumnData.getAlarmTime = newHours + ":" + newMins;
						 // save into db
						saveIntoDB();
					}  
		        }, setHour, setMinute, true).show();  
			}
			
		});
		
		alarmTimeZoneView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, TimeZoneActivity.class);
				Activity mActivity = (Activity) mContext;
				mActivity.startActivityForResult(intent, 100);
			}
			
		});
	}
	
	private static void saveIntoDB(){
		
		 DatabaseHelper db = new DatabaseHelper(mContext);
		 
		 String[] columns = {"title", 
							"switcher", 
							"alarm_time", 
							"alarm_timezone"};

		String[] values = {setColumnData.getTitle, 
						   setColumnData.getSwitcher + "", 
						   setColumnData.getAlarmTime, 
						   setColumnData.getAlarmTimeZone};
		
		db.update("timezone_alarm", Integer.parseInt(setColumnData.getId), columns, values);   
		db.close();
	}
	
	
	public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
        	String getSelectedTimeZone = data.getStringExtra("timezone_id");
        	setColumnData.getAlarmTimeZone = getSelectedTimeZone;
        	alarmTimeZoneView.setText(setColumnData.getAlarmTimeZone);
        	saveIntoDB();
        }
        
        Toast.makeText(mContext, "HI" + requestCode + "\n" + resultCode, 1000).show();
	}
}
