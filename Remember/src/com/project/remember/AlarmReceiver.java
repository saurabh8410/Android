package com.project.remember;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	// The app's AlarmManager, which rovides access to the system alarm
	// services.
	DBClass db;
	String a_id = "", row_id = "";
	int length = 0, table_id = 0;
	String title = "";
	String type = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		db = new DBClass(context);
		a_id = intent.getDataString();
		length = a_id.length();
		table_id = Integer.parseInt(String.valueOf(a_id.charAt(0)));
		if (table_id == 1) {
			row_id = a_id.substring(1, length);
			getTitle1(row_id);
			updateAlarmDataForTable1(context);
		} else {
			row_id = a_id.substring(1, length-1);
			getTitle2(row_id);
			updateAlarmDataForTable2(context);
		}

		Intent i = new Intent(context, Notify.class);
		i.putExtra("title", title);
		i.putExtra("type", type);
		i.putExtra("a_id", a_id);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	public void getTitle1(String id) {
		Cursor c = db.getRow(db.DATABASE_TABLE1, id);
		c.moveToFirst();
		title = c.getString(c.getColumnIndex(db.FIELD_NAME));
		type = c.getString(c.getColumnIndex(db.FIELD_TYPE));
	}

	public void getTitle2(String id) {
		Cursor c = db.getRow(db.DATABASE_TABLE2, id);
		c.moveToFirst();
		title = c.getString(c.getColumnIndex(db.FIELD_NAME));
		type = "Medicine";
	}

	// change alarm date time as they repeat in database
	public void updateAlarmDataForTable1(Context context) {
		Cursor c = db.getRow(db.DATABASE_TABLE1, row_id);
		c.moveToFirst();
		String repeat = c.getString(c.getColumnIndex(db.FIELD_REPEAT));
		Calendar cal = Calendar.getInstance();
		if (repeat.equals("Once")) {
			db.deleteData(db.DATABASE_TABLE1, row_id);
		} else {
			String date = c.getString(c.getColumnIndex(db.FIELD_ADATE));
			String d[] = db.splitDate(date);
			int y = Integer.parseInt(d[0]);
			int m = Integer.parseInt(d[1]);
			int day = Integer.parseInt(d[2]);

			if (repeat.equals("Daily")) {
				cal.set(Calendar.YEAR, y);
				cal.set(Calendar.MONTH, (m - 1));
				cal.set(Calendar.DAY_OF_MONTH, day + 1);
				y = cal.get(cal.YEAR);
				m = cal.get(cal.MONTH);
				day = cal.get(cal.DAY_OF_MONTH);
				Toast.makeText(context, day + "/" + m + "/" + y,
						Toast.LENGTH_LONG).show();
			} else if (repeat.equals("Weekly")) {
				cal.set(Calendar.YEAR, y);
				cal.set(Calendar.MONTH, (m - 1));
				cal.set(Calendar.DAY_OF_MONTH, day + 7);
				y = cal.get(cal.YEAR);
				m = cal.get(cal.MONTH);
				day = cal.get(cal.DAY_OF_MONTH);
				Toast.makeText(context, day + "/" + m + "/" + y,
						Toast.LENGTH_LONG).show();
			} else if (repeat.equals("Monthly")) {
				cal.set(Calendar.YEAR, y);
				cal.set(Calendar.MONTH, m);
				cal.set(Calendar.DAY_OF_MONTH, day);
				y = cal.get(cal.YEAR);
				m = cal.get(cal.MONTH);
				day = cal.get(cal.DAY_OF_MONTH);
				Toast.makeText(context, day + "/" + m + "/" + y,
						Toast.LENGTH_LONG).show();
			} else {
				cal.set(Calendar.YEAR, y + 1);
				cal.set(Calendar.MONTH, (m - 1));
				cal.set(Calendar.DAY_OF_MONTH, day);
				y = cal.get(cal.YEAR);
				m = cal.get(cal.MONTH);
				day = cal.get(cal.DAY_OF_MONTH);
				Toast.makeText(context, day + "/" + m + "/" + y,
						Toast.LENGTH_LONG).show();
			}
			date = db.dbDate(y, m, day);
			db.updateDate(db.DATABASE_TABLE1, row_id, date);
			removePendingIntents(context);
			setAlarmForTable1(context);
		}
	}
	
	//setting for medicine alarms
	private void updateAlarmDataForTable2(Context context) {
		// TODO Auto-generated method stub
		String  time2 = "", time3 = "";
		int y = 0, m = 0, day = 0, h = 0, min = 0;
		int time_id = Integer.parseInt(String.valueOf(a_id.charAt(length - 1)));
		Cursor c = db.getRow(db.DATABASE_TABLE2, row_id);
		c.moveToFirst();
		String repeat = c.getString(c.getColumnIndex(db.FIELD_REPEAT));
		String date = c.getString(c.getColumnIndex(db.FIELD_ADATE));
		String d[] = db.splitDate(date);
		y = Integer.parseInt(d[0]);
		m = Integer.parseInt(d[1]);
		day = Integer.parseInt(d[2]);

		time2 = c.getString(c.getColumnIndex(db.FIELD_TIME2));
		time3 = c.getString(c.getColumnIndex(db.FIELD_TIME1));
		Calendar cal = Calendar.getInstance();
		if (time_id == 3 || (time2.equals("null") && time3.equals("null"))) {
			if (repeat.equals("Once")) {
				db.deleteData(db.DATABASE_TABLE2, row_id);
			} else {
				if (repeat.equals("Daily")) {
					cal.set(Calendar.YEAR, y);
					cal.set(Calendar.MONTH, (m - 1));
					cal.set(Calendar.DAY_OF_MONTH, day + 1);
					y = cal.get(cal.YEAR);
					m = cal.get(cal.MONTH);
					day = cal.get(cal.DAY_OF_MONTH);
					Toast.makeText(context, day + "/" + m + "/" + y,
							Toast.LENGTH_LONG).show();
				} else if (repeat.equals("Weekly")) {
					cal.set(Calendar.YEAR, y);
					cal.set(Calendar.MONTH, (m - 1));
					cal.set(Calendar.DAY_OF_MONTH, day + 7);
					y = cal.get(cal.YEAR);
					m = cal.get(cal.MONTH);
					day = cal.get(cal.DAY_OF_MONTH);
					Toast.makeText(context, day + "/" + m + "/" + y,
							Toast.LENGTH_LONG).show();
				} else if (repeat.equals("Monthly")) {
					cal.set(Calendar.YEAR, y);
					cal.set(Calendar.MONTH, m);
					cal.set(Calendar.DAY_OF_MONTH, day);
					y = cal.get(cal.YEAR);
					m = cal.get(cal.MONTH);
					day = cal.get(cal.DAY_OF_MONTH);
					Toast.makeText(context, day + "/" + m + "/" + y,
							Toast.LENGTH_LONG).show();
				} else {
					cal.set(Calendar.YEAR, y + 1);
					cal.set(Calendar.MONTH, (m - 1));
					cal.set(Calendar.DAY_OF_MONTH, day);
					y = cal.get(cal.YEAR);
					m = cal.get(cal.MONTH);
					day = cal.get(cal.DAY_OF_MONTH);
					Toast.makeText(context, day + "/" + m + "/" + y,
							Toast.LENGTH_LONG).show();
				}
				date = db.dbDate(y, m, day);
				db.updateDate(db.DATABASE_TABLE2, row_id, date);
				removePendingIntentForTable2(context);
				setAlarmForTable2(context);
			}
		} else {
			String a_id1="";
			String a_id2="";
			String time="";
			String t[] = new String[2];
			if (time_id == 1) {
				time = c.getString(c.getColumnIndex(db.FIELD_TIME2));
				if (!time.equals("null")) {
					t = db.splitTime(time);
					h=Integer.parseInt(t[0]);
					min=Integer.parseInt(t[1]);
					cal.set(y,(m-1),day,h,min,00);
					a_id1="2"+row_id+"1";
					a_id2="2"+row_id+"2";
				}
				else{
					time = c.getString(c.getColumnIndex(db.FIELD_TIME3));
					if (!time.equals("null")) {
						t = db.splitTime(time);
						h=Integer.parseInt(t[0]);
						min=Integer.parseInt(t[1]);
						cal.set(y,(m-1),day,h,min,00);
						a_id1="2"+row_id+"1";
						a_id2="2"+row_id+"3";
					}
				}
			}
			else{
				time = c.getString(c.getColumnIndex(db.FIELD_TIME3));
				if (!time.equals("null")) {
					t = db.splitTime(time);
					h=Integer.parseInt(t[0]);
					min=Integer.parseInt(t[1]);
					cal.set(y,(m-1),day,h,min,00);
					a_id1="2"+row_id+"2";
					a_id2="2"+row_id+"3";
				}
			}
			deleteAlarm(context,a_id1);
			setAlarm(context,cal,a_id2);
		}
	}

	public void setAlarm(Context ourContext, Calendar cal, String id) {

		Intent intent = new Intent(ourContext, AlarmReceiver.class);
		intent.setData(Uri.parse(id));
		intent.setAction(String.valueOf(id));
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ourContext,
				Integer.parseInt(id), intent, 0);
		AlarmManager alarmManager = (AlarmManager) ourContext
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);

		// Enable {@code SampleBootReceiver} to automatically restart the alarm
		// when the // device is rebooted.

		ComponentName receiver = new ComponentName(ourContext,
				RebootReceiver.class);
		PackageManager pm = ourContext.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	public void deleteAlarm(Context ourContext, String id) {
		Intent intent = new Intent(ourContext, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ourContext,
				Integer.parseInt(id), intent, 0);
		AlarmManager alarmManager = (AlarmManager) ourContext
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

	public void setAlarmForTable1(Context context) {
		db = new DBClass(context);
		try {
			Cursor c = db.getRecentTaskForAlarm(db.DATABASE_TABLE1);
			c.moveToFirst();
			String date[] = db.splitDate(c.getString(c
					.getColumnIndex(db.FIELD_ADATE)));
			int y = Integer.parseInt(date[0]);
			int m = Integer.parseInt(date[1]);
			int d = Integer.parseInt(date[2]);
			String time[] = db.splitTime(c.getString(c
					.getColumnIndex(db.FIELD_ATIME)));
			int h = Integer.parseInt(time[0]);
			int min = Integer.parseInt(time[1]);
			row_id = c.getString(c.getColumnIndex(db.FIELD_ID));
			Calendar cal = Calendar.getInstance();
			cal.set(y, (m - 1), d, h, min, 00);
			String a_id = "1" + row_id;
			// setting alarm
			Intent intent = new Intent(context, AlarmReceiver.class);
			intent.setData(Uri.parse(a_id));
			intent.setAction(String.valueOf(a_id));
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					Integer.parseInt(a_id), intent, 0);
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					pendingIntent);

			// Enable {@code RebootReceiver} to automatically restart the
			// alarm when the
			// device is rebooted.

			ComponentName receiver = new ComponentName(context,
					RebootReceiver.class);
			PackageManager pm = context.getPackageManager();
			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);

		} catch (Exception e) {
		}
	}

	public void setAlarmForTable2(Context context) {
		db = new DBClass(context);
		try {
			Cursor c = db.getRecentTaskForAlarm(db.DATABASE_TABLE2);
			c.moveToFirst();
			String date[] = db.splitDate(c.getString(c
					.getColumnIndex(db.FIELD_ADATE)));
			int y = Integer.parseInt(date[0]);
			int m = Integer.parseInt(date[1]);
			int d = Integer.parseInt(date[2]);
			String time[] = db.splitTime(c.getString(c
					.getColumnIndex(db.FIELD_TIME1)));
			int h = Integer.parseInt(time[0]);
			int min = Integer.parseInt(time[1]);
			row_id = c.getString(c.getColumnIndex(db.FIELD_ID));
			Calendar cal = Calendar.getInstance();
			// for time1
			cal.set(y, (m - 1), d, h, min, 00);
			String a_id = "2" + row_id + "1";
			// setting alarm
			setAlarmForTable2ForDiffetentTime(context, cal, a_id);
			// for time2
			if (!c.getString(c.getColumnIndex(db.FIELD_TIME2)).equals("null")) {
				time = db.splitTime(c.getString(c
						.getColumnIndex(db.FIELD_TIME2)));
				h = Integer.parseInt(time[0]);
				min = Integer.parseInt(time[1]);
				cal = Calendar.getInstance();
				// for time1
				cal.set(y, (m - 1), d, h, min, 00);
				a_id = "2" + row_id + "2";
				// setting alarm
				setAlarmForTable2ForDiffetentTime(context, cal, a_id);
			}
			// for time3
			if (!c.getString(c.getColumnIndex(db.FIELD_TIME3)).equals("null")) {
				time = db.splitTime(c.getString(c
						.getColumnIndex(db.FIELD_TIME3)));
				h = Integer.parseInt(time[0]);
				min = Integer.parseInt(time[1]);
				cal = Calendar.getInstance();
				// for time1
				cal.set(y, (m - 1), d, h, min, 00);
				a_id = "2" + row_id + "3";
				// setting alarm
				setAlarmForTable2ForDiffetentTime(context, cal, a_id);
			}

			// Enable {@code RebootReceiver} to automatically restart the
			// alarm when the
			// device is rebooted.

			ComponentName receiver = new ComponentName(context,
					RebootReceiver.class);
			PackageManager pm = context.getPackageManager();
			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);

		} catch (Exception e) {	}
	}

	private void setAlarmForTable2ForDiffetentTime(Context context,
			Calendar cal, String a_id) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setData(Uri.parse(a_id));
		intent.setAction(String.valueOf(a_id));
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				Integer.parseInt(a_id), intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
	}

	public void removePendingIntents(Context ourContext) {
		try {
			String id = "";
			String a_id = "";
			db = new DBClass(ourContext);
			Intent intent = new Intent(ourContext, AlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					ourContext, Integer.parseInt("1" + id), intent, 0);
			AlarmManager alarmManager;
			Cursor c = db.getAllDataOfTable(db.DATABASE_TABLE1);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				id = c.getString(c.getColumnIndex(db.FIELD_ID));
				a_id = "1" + id;
				intent.setData(Uri.parse(a_id));
				intent.setAction(String.valueOf(a_id));
				pendingIntent = PendingIntent.getBroadcast(ourContext,
						Integer.parseInt(a_id), intent, 0);
				alarmManager = (AlarmManager) ourContext
						.getSystemService(Context.ALARM_SERVICE);
				alarmManager.cancel(pendingIntent);
				pendingIntent.cancel();
			}
		} catch (Exception e) {}
	}

	public void removePendingIntentForTable2(Context ourContext) {
		try {
			String id = "";
			String a_id1 = "", a_id2 = "", a_id3 = "";
			db = new DBClass(ourContext);
			Intent intent1 = new Intent(ourContext, AlarmReceiver.class);
			Intent intent2 = new Intent(ourContext, AlarmReceiver.class);
			Intent intent3 = new Intent(ourContext, AlarmReceiver.class);
			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
					ourContext, Integer.parseInt("2" + id), intent1, 0);
			PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
					ourContext, Integer.parseInt("2" + id), intent2, 0);
			PendingIntent pendingIntent3 = PendingIntent.getBroadcast(
					ourContext, Integer.parseInt("2" + id), intent3, 0);
			AlarmManager alarmManager;
			Cursor c = db.getAllDataOfTable(db.DATABASE_TABLE2);
			if (c != null) {
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					id = c.getString(c.getColumnIndex(db.FIELD_ID));
					a_id1 = "2" + id + "1";
					a_id2 = "2" + id + "2";
					a_id3 = "2" + id + "3";
					intent1.setData(Uri.parse(a_id1));
					intent1.setAction(String.valueOf(a_id1));
					pendingIntent1 = PendingIntent.getBroadcast(ourContext,
							Integer.parseInt(a_id1), intent1, 0);
					intent2.setData(Uri.parse(a_id2));
					intent2.setAction(String.valueOf(a_id2));
					pendingIntent2 = PendingIntent.getBroadcast(ourContext,
							Integer.parseInt(a_id2), intent2, 0);
					intent3.setData(Uri.parse(a_id3));
					intent3.setAction(String.valueOf(a_id3));
					pendingIntent3 = PendingIntent.getBroadcast(ourContext,
							Integer.parseInt(a_id3), intent3, 0);
					alarmManager = (AlarmManager) ourContext
							.getSystemService(Context.ALARM_SERVICE);
					alarmManager.cancel(pendingIntent1);
					alarmManager.cancel(pendingIntent2);
					alarmManager.cancel(pendingIntent3);
					pendingIntent1.cancel();
					pendingIntent2.cancel();
					pendingIntent3.cancel();
				}
			}
		} catch (Exception e) {}
	}
}
