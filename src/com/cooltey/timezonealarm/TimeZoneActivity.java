package com.cooltey.timezonealarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cooltey.timezonealarm.lib.DatabaseHelper;

public class TimeZoneActivity extends ActionBarActivity {
	
	private DatabaseHelper db;
	private ListView mainContent;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timezone);
		
		mContext = this;
		db = new DatabaseHelper(this);
		mainContent = (ListView) findViewById(R.id.scrollContent); 
		
		setListView();
		
		getSupportActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setListView(){
		try{			
			ArrayList<HashMap<String,String>> timezoneList = new ArrayList<HashMap<String,String>>();
			
			for(int i = 0; i < TimeZone.getAvailableIDs().length; i++){
				
				// create hash map to pust data
				HashMap<String,String> item = new HashMap<String,String>();
				
				TimeZone timezone   = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[i]);
                String timezoneName = TimeZone.getAvailableIDs()[i];
                int timeZoneOffset  = timezone.getRawOffset() / (60 * 1000);
                int timezoneGMT     = timeZoneOffset / 60;
                
				item.put("timezone_id", timezoneName);
				item.put("timezone_offset", timezoneGMT + "");
				timezoneList.add(item);
			}
			//ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ids);
			SimpleAdapter adapter = new SimpleAdapter(this,  timezoneList,
					 android.R.layout.simple_list_item_2,
					 new String[] { "timezone_id","timezone_offset" },
					 new int[] { android.R.id.text1, android.R.id.text2 } );
			mainContent.setAdapter(adapter);
			
			mainContent.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) view;
					String getTimeZone = tv.getText().toString();
					
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("timezone_id", getTimeZone);
					intent.putExtras(bundle);
					setResult(100, intent);
					finish();
				}
				
			});
		}catch(Exception e){
			Log.d("setListView", e + "");
		}
	}

}
