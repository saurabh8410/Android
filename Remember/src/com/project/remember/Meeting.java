package com.project.remember;

import java.util.Calendar;

import com.project.remember.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Meeting extends Activity implements OnClickListener {

	AlarmReceiver alarm=new AlarmReceiver();
	Button btnSave,btnMap;
	ImageButton btnSelectDate, btnSelectTime;
	TextView tvTime,tvDate;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	private int Year, Month, Day, Hour, Minute;
	EditText txtTitle, txtPlace;
	Spinner spRepeat;
	DBClass db;
	String row_id = "";
	Calendar current,cal;
	
	
	public Meeting() {
		// Assign current Date and Time Values to Variables
		final Calendar c = Calendar.getInstance();
		Year = c.get(Calendar.YEAR);
		Month = c.get(Calendar.MONTH);
		Day = c.get(Calendar.DAY_OF_MONTH);
		Hour = c.get(Calendar.HOUR_OF_DAY);
		Minute = c.get(Calendar.MINUTE);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_meeting);
		initalize();
		setEvents();
		db = new DBClass(this);
		setDate();
		this.overridePendingTransition(R.anim.flip_left_to_right,R.anim.zoom_out);
		checkForBundle();
	}
	
	private void checkForBundle() {
		// TODO Auto-generated method stub
		try {
			Bundle b = this.getIntent().getExtras();
			row_id = b.getString("row_id");
			getData(row_id);
			Toast.makeText(this, row_id, Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	private void setDate()
	{
		tvDate.setText(db.date(Year, Month, Day));
		tvTime.setText(db.time(Hour,Minute));
	}
	
	private void initalize() {
		// TODO Auto-generated method stub
		btnSelectDate = (ImageButton) findViewById(R.id.mtBtnSelectDate);
		btnSelectTime = (ImageButton) findViewById(R.id.mtBtnSelectTime);
		tvDate = (TextView) findViewById(R.id.mtTvDate);
		tvTime = (TextView) findViewById(R.id.mtTvTime);
		btnSave = (Button) findViewById(R.id.mtBtnSave);
		txtTitle = (EditText) this.findViewById(R.id.mtEtTitle);
		txtPlace = (EditText) this.findViewById(R.id.mtEtPlace);
		spRepeat=(Spinner)this.findViewById(R.id.mtSpRepeat);
		btnMap=(Button)this.findViewById(R.id.mtBtnMap);
	}
	private void setEvents() {
		// TODO Auto-generated method stub
		btnSave.setOnClickListener(this);
		btnSelectDate.setOnClickListener(this);
		btnSelectTime.setOnClickListener(this);
		btnMap.setOnClickListener(this);
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
			// Set the Selected Date in Select date Button
			tvTime.setText(db.time(h, m));
			Hour = h;
			Minute = m;
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
		switch(v.getId())
		{
		case R.id.mtBtnSave:
			if(txtTitle.getText().toString().equals("") || txtPlace.getText().toString().equals(""))
			{
			Toast.makeText(this, "Please Fill All Information", Toast.LENGTH_SHORT).show();}
			else {
				current = Calendar.getInstance();
				cal = Calendar.getInstance();
				cal.set(Year, Month, Day, Hour, Minute, 00);
				if (cal.compareTo(current) <= 0) {
					// The set Date/Time already passed
					Toast.makeText(getApplicationContext(),
							"Please Enter future Date/Time", Toast.LENGTH_LONG)
							.show();
				} else {
					if (row_id.equals("")) {
						insertData();
						finish();
					} else
						updateData(row_id);
				}
			}
			break;
		case R.id.mtBtnSelectTime:
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.mtBtnSelectDate:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.mtBtnMap:
			if(txtPlace.getText().toString().equals("")){
				Toast.makeText(this, "Please mention the place", Toast.LENGTH_LONG).show();
			}
			else{
				String address=txtPlace.getText().toString();
				String uri = "geo:0,0?q="+address;
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			}
 
		}
	}
	
	private void insertData()
	{
		try {
		String title = txtTitle.getText().toString();
		String place = txtPlace.getText().toString();
		String date = db.dbDate(Year, Month, Day);
		String time = db.dbTime(Hour, Minute);
		String type="Meeting";
		String repeat=spRepeat.getSelectedItem().toString();
		db.open();
		db.createEntry(title,type, place,repeat, date, time);
		Toast.makeText(Meeting.this, "Event Saved", Toast.LENGTH_SHORT).show();
		db.close();
		alarm.removePendingIntents(this);
		alarm.setAlarmForTable1(this);
		} catch (Exception e) {
		Toast.makeText(Meeting.this, e.toString(), Toast.LENGTH_SHORT).show();
		}
		finally{
			
		}
	}
	
	private void getData(String id) {
		Cursor c = db.getRow(db.DATABASE_TABLE1, id);
		c.moveToFirst();
		txtTitle.setText(c.getString(c.getColumnIndex(db.FIELD_NAME)));
		txtPlace.setText(c.getString(c.getColumnIndex(db.FIELD_INTEREST))
				.toString());
		tvDate.setText(db.convertDateInto12(c.getString(c
				.getColumnIndex(db.FIELD_ADATE))));
		tvTime.setText(db.convertTimeInto12(c.getString(c
				.getColumnIndex(db.FIELD_ATIME))));
		db.close();
	}
	
	private void updateData(String id) {
		try {
			String name = txtTitle.getText().toString();
			String place = txtPlace.getText().toString();
			String date = db.dbDate(Year, Month, Day);
			String time = db.dbTime(Hour, Minute);
			String type = "Meeting";
			String repeat=spRepeat.getSelectedItem().toString();
			db.open();
			db.updateEntry1(id,name, type, place, repeat, date, time);
			db.close();
			alarm.removePendingIntents(this);
			alarm.setAlarmForTable1(this);
			Toast.makeText(this, "Event Updated", Toast.LENGTH_SHORT).show();
			finish();
		} catch (Exception e) {	}
	}

}
