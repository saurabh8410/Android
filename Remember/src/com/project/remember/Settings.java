package com.project.remember;

import com.project.remember.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends Activity implements OnClickListener {

	SharedPreferences sharedPreferences;
	Spinner spColor, spTone;
	Button btnSave;
	public int color = 0, tone = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_setting);
		spColor = (Spinner) this.findViewById(R.id.settingSpColor);
		spTone = (Spinner) this.findViewById(R.id.settingSpTone);
		btnSave = (Button) this.findViewById(R.id.settingBtnSave);
		btnSave.setOnClickListener(this);
		getSharedData(this);
		setSettings();
	}

	private void setSettings() {
		// TODO Auto-generated method stub
		spColor.setSelection(color);
		spTone.setSelection(tone);
	}

	private void saveSharedData(int color, int tone) {
		// TODO Auto-generated method stub
		sharedPreferences = this.getSharedPreferences("Common_Data", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("TileColor", color);
		editor.putInt("AlarmTone", tone);
		editor.commit();
	}

	public void getSharedData(Context context){
		try{
			sharedPreferences=context.getSharedPreferences("Common_Data", Context.MODE_PRIVATE);
			color=sharedPreferences.getInt("TileColor", 0); //(key,default value)
			tone=sharedPreferences.getInt("AlarmTone", 0);
		}catch(Exception e){	}		
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		color = spColor.getSelectedItemPosition();
		tone = spTone.getSelectedItemPosition();
		saveSharedData(color, tone);
		Toast.makeText(this, "Settings Saved", Toast.LENGTH_LONG).show();
		finish();
	}

}
