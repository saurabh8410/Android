package com.project.remember;

import com.project.remember.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment implements OnClickListener {

	

	public HomeFragment() {
	}

	View rootView;
	Animation anim;
	ImageButton btnBirthday, btnAni, btnMeeting, btnTask, btnMedicine,btnEvents;
	LinearLayout layout_home;
	Intent ourIntent;
	Settings setting;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		anim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.vibrarte_animation);
	
		init();
		setEvents();
		animation();
		return rootView;
	}

	private void init() {
		btnBirthday = (ImageButton) rootView.findViewById(R.id.btnBirth);
		btnAni = (ImageButton) rootView.findViewById(R.id.btnAni);
		btnMeeting = (ImageButton) rootView.findViewById(R.id.btnMeeting);
		btnTask = (ImageButton) rootView.findViewById(R.id.btnTask);
		btnEvents = (ImageButton) rootView.findViewById(R.id.btnEvents);
		btnMedicine = (ImageButton) rootView.findViewById(R.id.btnMedicine);
		layout_home=(LinearLayout)rootView.findViewById(R.id.layout_home);
		applySettings();
	}
	
	private void applySettings(){
		setting=new Settings();
		DBClass db=new DBClass(rootView.getContext());
		setting.getSharedData(rootView.getContext());
		int c=db.color[setting.color];
		layout_home.setBackgroundColor(c);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		applySettings();
	}
	
	private void setEvents() {
		// TODO Auto-generated method stub
		btnBirthday.setOnClickListener(this);
		btnAni.setOnClickListener(this);
		btnTask.setOnClickListener(this);
		btnEvents.setOnClickListener(this);
		btnMedicine.setOnClickListener(this);
		btnMeeting.setOnClickListener(this);
	}

	private void animation() {
		btnBirthday.startAnimation(anim);
		btnMeeting.startAnimation(anim);
		btnTask.startAnimation(anim);
		btnMedicine.startAnimation(anim);
		btnAni.startAnimation(anim);
		btnEvents.startAnimation(anim);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String action="android.intent.action.";
		switch (arg0.getId()) {
		case R.id.btnBirth:
				action+="BIRTHDAY";
			break;
		case R.id.btnAni:
				action+="ANNIVERSARY";
			break;
		case R.id.btnMeeting:
			action+="MEETING";
			break;
		case R.id.btnTask:
			action+="TASK";
			break;
		case R.id.btnMedicine:
			action+="MEDICINE";
			break;
		case R.id.btnEvents:
			action+="EVENTS";
			break;
		}
		ourIntent=new Intent(action);
		startActivity(ourIntent);
	}
}
