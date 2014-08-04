package com.cooltey.timezonealarm;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.cooltey.timezonealarm.lib.ColumnData;
import com.cooltey.timezonealarm.lib.DatabaseHelper;

public class AlarmReceiver extends BroadcastReceiver {	
	
	private ColumnData setColumnData = new ColumnData();
	private DatabaseHelper db;
	private Calendar mCalendar;
	private Context mContext;
	private NotificationManager notificationManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		mContext = context;
		db = new DatabaseHelper(context);
		mCalendar = Calendar.getInstance();
		notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// do receiver action
		//Log.d("LazyMaster", "HELLO")
		setCursorData();
		
	}
	
	private String getCurrentTime(String timezone){

		// TODO Auto-generated method stub
		// set timezone
		mCalendar.setTimeZone(TimeZone.getTimeZone(timezone));
		
		int setHour 	  = mCalendar.get(Calendar.HOUR_OF_DAY);
		int setMinute	  =	mCalendar.get(Calendar.MINUTE);
		
		String currentHour = "" + setHour;
		String currentMinute = "" + setMinute;
		if(setHour < 10){
			currentHour = "0" + setHour;
		}
		
		if(setMinute < 10){
			currentMinute = "0" + setMinute;
		}
		

		String finalCurrentTime = currentHour + ":" + currentMinute;
		
		return finalCurrentTime;
	}
	
	private void setCursorData(){
		try{
			Cursor getData = db.getAll("timezone_alarm", " WHERE 1=1 AND switcher = 'true'");
			boolean isActivated = false;
			if(getData != null){				
				getData.moveToFirst();
				
				for(int i = 0; i < getData.getCount(); i++){
					String getAlarmTime 	= getData.getString(3);
					String getAlarmTimeZone = getData.getString(4);
					Log.d("getAlarmTime", getAlarmTime);
					Log.d("getAlarmTimeZone", getAlarmTimeZone);
					if(getAlarmTime.equalsIgnoreCase(getCurrentTime(getAlarmTimeZone))){
						// start alarm
						startSetUp(getData);
					}
					getData.moveToNext(); 
				}
			}else{
				Log.d("backToDefault", "true");
			}
			
			if(!isActivated){
				Log.d("backToDefault", "true");
			}
		}catch(Exception e){
			Log.d("getDatabaseData", e + "");
		}
	}
	
	private void startSetUp(Cursor cursor){

		SharedPreferences settings = mContext.getSharedPreferences("system_info", 0);	
		boolean isActivated = settings.getBoolean("activated", false);
		int activateId = settings.getInt("task_id", 0);
		if(!isActivated || activateId !=  cursor.getInt(0)){
			setColumnData.getTitle 			= cursor.getString(1);
			
			// use intent to activate brightness change			
		    SharedPreferences.Editor getData = settings.edit();
		    getData.putBoolean("activated", true);
		    getData.commit();
		    
		    generateNotification(mContext, 
		    		setColumnData.getTitle, 
		    		mContext.getString(R.string.notification_msg));
		}
	}
	
	
	private void generateNotification(Context context, String title, String message) {
		
	   //Creating Notification Builder
	   NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
       //Title for Notification
       notification.setContentTitle(title);
       //Message in the Notification
       notification.setContentText(message);
       //Alert shown when Notification is received
       notification.setTicker(title);
       //Icon to be set on Notification
       notification.setSmallIcon(R.drawable.ic_launcher);
       //Creating new Stack Builder
       TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
       stackBuilder.addParentStack(MainActivity.class);
       //Intent which is opened when notification is clicked
       Intent resultIntent = new Intent(context, MainActivity.class);
       stackBuilder.addNextIntent(resultIntent);
       PendingIntent pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
       notification.setContentIntent(pIntent);
       notificationManager.notify(0, notification.build());
	}
}