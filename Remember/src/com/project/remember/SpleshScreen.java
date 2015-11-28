package com.project.remember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SpleshScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);	//To hide title bar
		this.setContentView(R.layout.activty_splash);
		Thread splash= new Thread(){
			public void run(){
				try{
					sleep(3000);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					startActivity(new Intent("android.intent.action.MainActivity"));
				}
			}
		};
		splash.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	
	
}
