package com.project.remember;


import java.util.Calendar;

import com.project.remember.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
public class DBClass {
	final static int ROS_1 = 20;
	public String[] months=new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
	public int[] color=new int[]{Color.CYAN,Color.GREEN,Color.BLUE,Color.TRANSPARENT,Color.RED};
	public int[] tone=new int[]{R.raw.tone1,R.raw.tone2,R.raw.tone3,R.raw.tone4};
	private int Y, M, D, H, Min;
	String date="";
	public static final String DATABASE_NAME = "db_Remember";
	public static int DATABASE_VERSION = 1;
	
	// database tables
	public static String DATABASE_TABLE1 = "tblevents";
	public static String DATABASE_TABLE2 = "tblmedicine";
	
	
//common fields of tables
	public static String FIELD_ID = "_id";
	public static String FIELD_NAME = "title";
	public static String FIELD_TYPE = "type";
	
	//fields of birthday,anniversary,meeting,task
	public static String FIELD_INTEREST = "interest";
	public static String FIELD_ADATE = "alarm_date";
	public static String FIELD_ATIME = "alarm_time";
	public static String FIELD_REPEAT="repeat";

	// fields of medicine table
	public static String FIELD_TIME1 = "time1";
	public static String FIELD_TIME2 = "time2";
	public static String FIELD_TIME3 = "time3";

	public DbProcess ourHelper;
	public static Context ourContext;
	public SQLiteDatabase ourDatabase;

	public DBClass(Context c) {
		ourContext = c;
	}

	public static class DbProcess extends SQLiteOpenHelper {

		public DbProcess(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE1 + " (" + FIELD_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIELD_NAME
					+ " TEXT NOT NULL," + FIELD_TYPE + " TEXT NOT NULL,"
					+ FIELD_INTEREST + " TEXT NOT NULL," + FIELD_ADATE
					+ " TEXT NOT NULL," + FIELD_ATIME + " TEXT NOT NULL,"+FIELD_REPEAT+" TEXT NOT NULL);");
			

			db.execSQL("CREATE TABLE " + DATABASE_TABLE2 + " (" + FIELD_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIELD_NAME
					+ " TEXT NOT NULL,"+FIELD_ADATE
					+ " TEXT NOT NULL," + FIELD_TIME1 + " TEXT,"
					+ FIELD_TIME2 + " TEXT," + FIELD_TIME3
					+ " TEXT,"+FIELD_REPEAT+" TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE1);
			db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE2);
			onCreate(db);
		}

	}

	public DBClass open() throws Exception {
		ourHelper = new DbProcess(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	//method for inserting data of birthday,meeting,anniversary,task
	public long createEntry(String name, String type, String interest,String repeat,
			String date, String time) {

		ContentValues cv = new ContentValues();
		cv.put(FIELD_NAME, name);
		cv.put(FIELD_ADATE, date);
		cv.put(FIELD_ATIME, time);
		cv.put(FIELD_TYPE, type);
		cv.put(FIELD_INTEREST, interest);
		cv.put(FIELD_REPEAT, repeat);
		return ourDatabase.insert(DATABASE_TABLE1, null, cv);
	}
	
	public long updateEntry1(String id,String name, String type, String interest,String repeat,
			String date, String time) {

		ContentValues cv = new ContentValues();
		cv.put(FIELD_NAME, name);
		cv.put(FIELD_ADATE, date);
		cv.put(FIELD_ATIME, time);
		cv.put(FIELD_TYPE, type);
		cv.put(FIELD_INTEREST, interest);
		cv.put(FIELD_REPEAT, repeat);
		return ourDatabase.update(DATABASE_TABLE1, cv, FIELD_ID+"="+id, null);
	}
	public long updateEntry2(String id,String name,String date,String time1,String time2,String time3,String repeat) {

		ContentValues cv = new ContentValues();
		cv.put(FIELD_NAME, name);
		cv.put(FIELD_ADATE, date);
		cv.put(FIELD_TIME1, time1);
		cv.put(FIELD_TIME2, time2);
		cv.put(FIELD_TIME3, time3);
		cv.put(FIELD_REPEAT, repeat);
		return ourDatabase.update(DATABASE_TABLE2, cv, FIELD_ID+"="+id, null);
	}
	
	//method for inserting data of medicine table
	public long createEntryForMedicine(String name,String date, String time1, String time2,
			String time3,String repeat) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(FIELD_NAME, name);
		cv.put(FIELD_ADATE, date);
		cv.put(FIELD_TIME1, time1);
		cv.put(FIELD_TIME2, time2);
		cv.put(FIELD_TIME3, time3);
		cv.put(FIELD_REPEAT, repeat);
		return ourDatabase.insert(DATABASE_TABLE2, null, cv);
	}

	public Cursor queryForBd() {
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Cursor c=ourDatabase.rawQuery("select * from "+DATABASE_TABLE1+" where "+FIELD_TYPE+"=?"+" ORDER BY "+FIELD_ADATE+","+FIELD_ATIME+" ASC",new String[]{"Birthday"});
		return c;
	}
	public Cursor queryForTask() {
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Cursor c=ourDatabase.rawQuery("select * from "+DATABASE_TABLE1+" where "+FIELD_TYPE+"=?"+" ORDER BY "+FIELD_ADATE+","+FIELD_ATIME+" ASC",new String[]{"Task"});
		return c;
	}
	
	public Cursor queryForAni() {
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor c=ourDatabase.rawQuery("select * from "+DATABASE_TABLE1+" where "+FIELD_TYPE+"=?"+" ORDER BY "+FIELD_ADATE+","+FIELD_ATIME+" ASC",new String[]{"Anniversary"});
		return c;
	}
	public Cursor queryForMeeting() {
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor c=ourDatabase.rawQuery("select * from "+DATABASE_TABLE1+" where "+FIELD_TYPE+"=?"+" ORDER BY "+FIELD_ADATE+","+FIELD_ATIME+" ASC",new String[]{"Meeting"});
		return c;
	}
	public Cursor queryForMedi() {
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor c=ourDatabase.rawQuery("select * from "+DATABASE_TABLE2+" ORDER BY "+FIELD_ADATE+","+FIELD_TIME1+" ASC",null);
		return c;
	}
	
	public void deleteData(String id)
	{
		ourDatabase.delete(DATABASE_TABLE1, FIELD_ID+"="+id, null);
	}

	public void deleteData2(String id) {
		// TODO Auto-generated method stub
		ourDatabase.delete(DATABASE_TABLE2, FIELD_ID+"="+id, null);
	}
	
	public String time(int hour, int m){
		String time="";
		String min=""+m;
		if(m<10)
			min="0"+m;
		if(hour>12)
		{
			time=hour-12 + " : "+ min + " PM";
		}
		else if(hour==0)
		{
			time=12 + " : "+ min + " AM";
		}
		else if(hour==12)
		{
			time=12 + " : "+ min + " PM";
		}
		else
		{
			time=hour + " : "+ min + " AM";
		}
		return time;
	}
	public String dbTime(int h, int m){
		String time="";
		String hour=""+h;
		if(h<10)
			hour="0"+h;
		time=hour+":"+m;
		return time;
	}
	public String dbDate(int y,int m,int d){
		String date="";
			date=y+"-"+(m+1)+"-"+d;
		return date;
	}
	public String date(int y,int m,int d){
		String date="";
			date=months[m]+","+d+","+y;
		return date;
	}
	public String convertTimeInto12(String t){
		String hm[]=t.split(":");
		String time=time(Integer.parseInt(hm[0]),Integer.parseInt(hm[1]));
		return time;
	}
	public String convertDateInto12(String t){
		String d[]=t.split("-");
		String date=date(Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2]));
		return date;
	}
	public void getDateTime(){
		final Calendar c = Calendar.getInstance();
		Y = c.get(Calendar.YEAR);
		M = c.get(Calendar.MONTH);
		D = c.get(Calendar.DAY_OF_MONTH);
		H = c.get(Calendar.HOUR_OF_DAY);
		Min= c.get(Calendar.MINUTE);
	}
	
	public String getRowID(String table){
		
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor c=ourDatabase.rawQuery("select * from "+table+" ORDER BY "+FIELD_ID+" DESC LIMIT 1 ",null);
		c.moveToFirst();
		close();
		return c.getString(c.getColumnIndex(FIELD_ID));
	}
	
	public Cursor getRow(String table, String id){
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor c=ourDatabase.rawQuery("select * from "+table+" where "+FIELD_ID+"=?",new String[]{id});
		return c;
	}
	
	
	public Cursor getAllDataOfTable(String table){
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor c=ourDatabase.rawQuery("select * from "+table,null);
		return c;
	}
	public String[] splitDate(String date){
		String d[]=date.split("-");
		return d;
	}
	public String[] splitTime(String time){
		String t[]=time.split(":");
		return t;
	}
	
	public Cursor getDataForAlarmFind(String table)
	{
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Cursor c=ourDatabase.rawQuery("select * from "+table+" ORDER BY "+FIELD_ADATE+","+FIELD_ATIME+" ASC Limit 1",null);
		return c;
	}
	//for setting alarms
	public Cursor getRecentTaskForAlarm(String table){
		Cursor c;
		try {
			open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(table.equals(DATABASE_TABLE1))
		c=ourDatabase.rawQuery("select * from "+table+" ORDER BY "+FIELD_ADATE+","+FIELD_ATIME+" ASC Limit 1",null);
		else
			c=ourDatabase.rawQuery("select * from "+table+" ORDER BY "+FIELD_ADATE+","+FIELD_TIME1+" ASC Limit 1",null);
		
		return c;
	}
	//for deleting data in receiver
	public void deleteData(String table,String id)
	{
		ourDatabase.delete(table, FIELD_ID+"="+id, null);
	}
	//for update date in receiver
	public void updateDate(String table,String id,String date){
		ContentValues cv = new ContentValues();
		cv.put(FIELD_ADATE,date );
		ourDatabase.update(table, cv, FIELD_ID+"="+id, null);
	}
}
