package com.cooltey.timezonealarm;

import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cooltey.timezonealarm.lib.DatabaseHelper;
import com.cooltey.timezonealarm.lib.MainActivityItem;

public class MainActivity extends ActionBarActivity {
	
	private DatabaseHelper db;
	private LinearLayout mainContent;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		db = new DatabaseHelper(this);
		mainContent = (LinearLayout) findViewById(R.id.scrollContent); 
		
		setListView();
		
		setReceiver();
		getSupportActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setReceiver(){
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        // set receiver action
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        // set timer
        alarmManager.setRepeating(AlarmManager.RTC, 0, 10 * 1000, pendingIntent);
	}
	
	private void setListView(){
		try{
			mainContent.removeAllViewsInLayout();
			Cursor getData = db.getAll("timezone_alarm", "");
			if(getData != null){
				getData.moveToFirst();
				
				for(int i = 0; i < getData.getCount(); i++){
					MainActivityItem ci = new MainActivityItem(this, getData);
					View getView = ci.getItemView();
					
					mainContent.addView(getView);
					
					getData.moveToNext(); 
				}
			}
		}catch(Exception e){
			Log.d("setListView", e + "");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add:	

	        	String[] columns = {"title", 
	        						"switcher", 
	        						"alarm_time", 
	        						"alarm_timezone"};
	        	
	        	String[] values = {"", 
								   "false", 
								   "00:00", 
								   TimeZone.getDefault().getDisplayName() + ""};
	        	
	        	db.insert("timezone_alarm", columns, values);     
	        	
	        	// reset view
	        	setListView();
	            return true;
	            
	        case R.id.action_about:
	        	
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        MainActivityItem.onActivityResult(resultCode, resultCode, data);
	}
}
