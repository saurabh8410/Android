package com.project.remember;


import java.util.Calendar;

import com.project.remember.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Medicine extends Activity implements OnClickListener {
	AlarmReceiver alarm = new AlarmReceiver();
	Button btnSave;
	ImageButton btnSelectDate, btnSelectTime1, btnSelectTime2, btnSelectTime3;
	TextView tvDate, tvTime1, tvTime2, tvTime3;
	ToggleButton toggle2, toggle3;
	private int Year, Month, Day, Hour, Minute;
	EditText txtMediName;
	Spinner spRepeat;
	Toast t;
	DBClass db;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	String dbTime1 = "", dbTime2 = "", dbTime3;
	String row_id = "";
	Calendar current, cal1, cal2, cal3;
	static int time_id = 1;

	public Medicine() {
		final Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH);
		Day = c.get(Calendar.DAY_OF_MONTH);
		Hour = c.get(Calendar.HOUR_OF_DAY);
		Minute = c.get(Calendar.MINUTE);
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine);
		db = new DBClass(this);
		initalize();
		setEvents();
		setTime();
		checkForBundle();
		// this.overridePendingTransition(R.anim.flip_left_to_right,R.anim.zoom_out);
	}

	private void checkForBundle() {
		// TODO Auto-generated method stub
		try {
			Bundle b = this.getIntent().getExtras();
			row_id = b.getString("row_id");
			getData(row_id);
			Toast.makeText(getApplicationContext(), row_id, Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private void initalize() {
		// TODO Auto-generated method stub
		btnSelectTime1 = (ImageButton) findViewById(R.id.mediBtnSelectTime1);
		btnSelectTime2 = (ImageButton) findViewById(R.id.mediBtnSelectTime2);
		btnSelectTime3 = (ImageButton) findViewById(R.id.mediBtnSelectTime3);
		txtMediName = (EditText) this.findViewById(R.id.mediTxtName);
		tvDate = (TextView) this.findViewById(R.id.mediTvDate);
		tvTime1 = (TextView) findViewById(R.id.mediTvTime1);
		tvTime2 = (TextView) findViewById(R.id.mediTvTime2);
		tvTime3 = (TextView) findViewById(R.id.mediTvTime3);
		btnSave = (Button) findViewById(R.id.mediBtnSave);
		spRepeat = (Spinner) this.findViewById(R.id.mediSpRepeat);
		btnSelectDate = (ImageButton) this.findViewById(R.id.mediBtnSelectDate);
		toggle2 = (ToggleButton) this.findViewById(R.id.toggle2);
		toggle3 = (ToggleButton) this.findViewById(R.id.toggle3);
		btnSelectTime2.setEnabled(false);
		btnSelectTime3.setEnabled(false);
	}

	private void setEvents() {
		// TODO Auto-generated method stub
		btnSave.setOnClickListener(this);
		btnSelectTime1.setOnClickListener(this);
		btnSelectTime2.setOnClickListener(this);
		btnSelectTime3.setOnClickListener(this);
		btnSelectDate.setOnClickListener(this);
		toggle2.setOnClickListener(this);
		toggle3.setOnClickListener(this);
	}

	private void setTime() {
		tvDate.setText(db.date(Year, Month, Day));
		tvTime1.setText(db.time(Hour, Minute));
		tvTime2.setText(db.time(Hour, Minute));
		tvTime3.setText(db.time(Hour, Minute));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// the callback received when the user "sets" the Date in the
		// DatePickerDialog
		public void onDateSet(DatePicker view, int y, int m, int d) {
			tvDate.setText(db.date(y, m, d));
			Year = y;
			Month = m;
			Day = d;
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		// the callback received when the user "sets" the TimePickerDialog in
		// the dialog
		public void onTimeSet(TimePicker view, int h, int m) {
			switch (time_id) {
			case 1:
				tvTime1.setText(db.time(h, m));
				dbTime1 = db.dbTime(h, m);
				Hour = h;
				Minute = m;
				break;
			case 2:
				tvTime2.setText(db.time(h, m));
				dbTime2 = db.dbTime(h, m);
				break;
			case 3:
				tvTime3.setText(db.time(h, m));
				dbTime3 = db.dbTime(h, m);
				break;
			}

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// create a new DatePickerDialog with values you want to show
			return new DatePickerDialog(this, mDateSetListener, Year, Month,
					Day);
			// create a new TimePickerDialog with values you want to show
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, Hour, Minute,
					false);
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mediBtnSave:
			if (row_id.equals("")) {	
				if (txtMediName.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(),
							"Please Fill All Information", Toast.LENGTH_SHORT)
							.show();
				}
				else {
					if (toggle2.isChecked()) {
						String time[] = db.splitTime(dbTime2);
						Toast.makeText(this, time[0] + ":" + time[1],
								Toast.LENGTH_LONG).show();
						cal2 = Calendar.getInstance();
						cal2.set(Year, Month, Day, Integer.parseInt(time[0]),
								Integer.parseInt(time[1]), 00);
						current = Calendar.getInstance();
						if (cal2.compareTo(current) <= 0) {
							// The set Date/Time already passed
							Toast.makeText(getApplicationContext(),
									"Please Enter future Date/Time",
									Toast.LENGTH_LONG).show();
							return;
						}				
					}
					if (toggle3.isChecked()) {
						String time[] = db.splitTime(dbTime3);
						cal3 = Calendar.getInstance();
						cal3.set(Year, Month, Day, Integer.parseInt(time[0]),
								Integer.parseInt(time[1]), 00);
						current = Calendar.getInstance();
						if (cal3.compareTo(current) <= 0) {
							// The set Date/Time already passed
							Toast.makeText(getApplicationContext(),
									"Please Enter future Date/Time",
									Toast.LENGTH_LONG).show();
							return;
						}
					}
					current = Calendar.getInstance();
					cal1 = Calendar.getInstance();
					cal1.set(Year, Month, Day, Hour, Minute, 00);
					if (cal1.compareTo(current) <= 0) {
						// The set Date/Time already passed
						Toast.makeText(getApplicationContext(),
								"Please Enter future Date/Time",
								Toast.LENGTH_LONG).show();
						return;
					}else{
						insertData();
						finish();
					}
				}
			} else {
				updateData(row_id);
				finish();
			}
			break;
		case R.id.mediBtnSelectDate:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.mediBtnSelectTime1:
			time_id = 1;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.mediBtnSelectTime2:
			time_id = 2;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.mediBtnSelectTime3:
			time_id = 3;
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.toggle2:
			if (toggle2.isChecked()) {
				btnSelectTime2.setEnabled(true);
			} else
				btnSelectTime2.setEnabled(false);
			break;
		case R.id.toggle3:
			if (toggle3.isChecked()) {
				btnSelectTime3.setEnabled(true);
			} else
				btnSelectTime3.setEnabled(false);
			break;
		}
	}

	private void updateData(String id) {
		// TODO Auto-generated method stub

		String time1="", time2 = "", time3 = "";
		String time[]=new String[3];
		Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_SHORT)
				.show();
		try {
			String name = txtMediName.getText().toString();
			String date = db.dbDate(Year, Month, Day);
			time1 = dbTime1;
			time=db.splitTime(time1);
			current = Calendar.getInstance();
			cal1 = Calendar.getInstance();
			cal1.set(Year, Month, Day, Integer.parseInt(time[0]), Integer.parseInt(time[1]), 00);
			if (cal1.compareTo(current) <= 0) {
				// The set Date/Time already passed
				Toast.makeText(getApplicationContext(),
						"Please Enter future Date/Time",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (toggle2.isChecked()){
				time2 = dbTime2;
				time=db.splitTime(time2);
				current = Calendar.getInstance();
				cal2 = Calendar.getInstance();
				cal2.set(Year, Month, Day, Integer.parseInt(time[0]), Integer.parseInt(time[1]), 00);
				if (cal2.compareTo(current) <= 0) {
					// The set Date/Time already passed
					Toast.makeText(getApplicationContext(),
							"Please Enter future Date/Time",
							Toast.LENGTH_LONG).show();
					return;
				}
			}
			else
				time2 = "null";
			if (toggle3.isChecked())
			{
				time3 = dbTime3;
				time=db.splitTime(time3);
				current = Calendar.getInstance();
				cal3 = Calendar.getInstance();
				cal3.set(Year, Month, Day, Integer.parseInt(time[0]), Integer.parseInt(time[1]), 00);
				if (cal3.compareTo(current) <= 0) {
					// The set Date/Time already passed
					Toast.makeText(getApplicationContext(),
							"Please Enter future Date/Time",
							Toast.LENGTH_LONG).show();
					return;
				}
			}
			else
				time3 = "null";
			String repeat = spRepeat.getSelectedItem().toString();
			db.open();
			db.updateEntry2(id, name, date, time1, time2, time3, repeat);
			db.close();
			alarm.removePendingIntents(this);
			alarm.setAlarmForTable2(this);
			Toast.makeText(this, "Event Updated", Toast.LENGTH_SHORT).show();
			finish();
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		} finally {

		}
	}

	private void insertData() {
		String time2 = "", time3 = "";
		try {
			String name = txtMediName.getText().toString();
			String date = db.dbDate(Year, Month, Day);
			String time1 = dbTime1;
			if (toggle2.isChecked())
				time2 = dbTime2;
			else
				time2 = "null";
			if (toggle3.isChecked())
				time3 = dbTime3;
			else
				time3 = "null";
			String repeat = spRepeat.getSelectedItem().toString();
			db.open();
			db.createEntryForMedicine(name, date, time1, time2, time3, repeat);
			Toast.makeText(getApplicationContext(), "Event Saved",
					Toast.LENGTH_SHORT).show();
			db.close();
			alarm.removePendingIntentForTable2(this);
			alarm.setAlarmForTable2(this);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getData(String id) {
		Cursor c = db.getRow(db.DATABASE_TABLE2, id);
		c.moveToFirst();
		txtMediName.setText(c.getString(c.getColumnIndex(db.FIELD_NAME)));
		tvDate.setText(db.convertDateInto12(c.getString(c
				.getColumnIndex(db.FIELD_ADATE))));
		tvTime1.setText(db.convertTimeInto12(c.getString(c
				.getColumnIndex(db.FIELD_TIME1))));
		tvTime2.setText(db.convertTimeInto12(c.getString(c
				.getColumnIndex(db.FIELD_TIME2))));
		tvTime3.setText(db.convertTimeInto12(c.getString(c
				.getColumnIndex(db.FIELD_TIME3))));
		db.close();
	}
}
