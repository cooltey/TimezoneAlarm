package com.cooltey.timezonealarm.lib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

	public class DatabaseHelper extends SQLiteOpenHelper 
	{
		private static final String DATABASE = "database.db";
		
		private static final int DATABASEVERSION = 1;
		
		private SQLiteDatabase db;
		
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE, null, DATABASEVERSION);
			db = this.getWritableDatabase();
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			String DATABASE_CREATE_TABLE = "CREATE TABLE timezone_alarm (_ID INTEGER PRIMARY KEY, " +
					" title TEXT, " +
					" switcher TEXT, " +
					" alarm_time TEXT, " +
					" alarm_timezone TEXT);";
			
			db.execSQL(DATABASE_CREATE_TABLE);		
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS timezone_alarm");	
			onCreate(db);
		}
		
		public Cursor getAll(String tableName, String whereStr) 
		{
			Log.d("DB Query", "SELECT * FROM " + tableName + whereStr);
		    return db.rawQuery("SELECT * FROM " + tableName + whereStr, null);
		}
		
		public long insert(String tableName, String[] column, String[] value) 
		{
			ContentValues args = new ContentValues();
			for(int i = 0; i < column.length; i++)
			{
				args.put(column[i], value[i]);	
			}
	 
			return db.insert(tableName, null, args);
	    }
		
		public int delete(String tableName, long rowId) 
		{
			return db.delete(tableName,		
			"_ID = " + rowId,				
			null							
			);
		}
		
		public int update(String tableName, long rowId, String[] column, String[] value) 
		{
			ContentValues args = new ContentValues();
			for(int i = 0; i < column.length; i++)
			{
				args.put(column[i], value[i]);	
			}
			return db.update(tableName,	
			args,						
			"_ID=" + rowId,				
			null						
			);
		}
	}
