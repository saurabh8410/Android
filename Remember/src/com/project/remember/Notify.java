package com.project.remember;

import com.project.remember.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Notify extends Activity implements OnClickListener {

	MediaPlayer mp = null;
	DBClass db;
	TextView tvTitle;
	Button btnStop;
	ImageView ivIcon;
	public static int NOTIFICATION_ID=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.notify);
		initilize();
		String a_id = this.getIntent().getStringExtra("a_id");
		String title = this.getIntent().getStringExtra("title");
		String type = this.getIntent().getStringExtra("type");
		if(type.equals("Anniversary"))
			ivIcon.setBackgroundResource(R.drawable.ic_anni);
		else if(type.equals("Medicine"))
			ivIcon.setBackgroundResource(R.drawable.ic_medicine);
		else if(type.equals("Meeting"))
			ivIcon.setBackgroundResource(R.drawable.ic_meeting);
		else if(type.equals("Task"))
			ivIcon.setBackgroundResource(R.drawable.ic_task);
		else
			ivIcon.setBackgroundResource(R.drawable.ic_birthday);
		
		NOTIFICATION_ID=Integer.parseInt(a_id);
		playSound();
		tvTitle.setText(title);
		sendNotification(title,type);
	}
	
	private void playSound() {
		// TODO Auto-generated method stub
		Settings settings = new Settings();
		DBClass db=new DBClass(this);
		settings.getSharedData(this);
		int tone=db.tone[settings.tone];
		mp = MediaPlayer.create(this,tone);
		mp.setLooping(true);
		mp.start();
	}
	
	public void initilize(){
		btnStop=(Button)this.findViewById(R.id.notiBtnStop);
		btnStop.setOnClickListener(this);
		ivIcon=(ImageView)this.findViewById(R.id.imageView1);
		tvTitle = (TextView) this.findViewById(R.id.tvNotifyTitle);
		db = new DBClass(this);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
			mp.release();
			finish();
	}

	 private void sendNotification(String title,String type) {
		 NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);	
		 mBuilder.setSmallIcon(R.drawable.ic_launcher);
		 mBuilder.setContentTitle(type+"-"+title);
		 mBuilder.setContentText("You have a Task to do.");
		 NotificationManager mNotificationManager =
		(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// notificationID allows you to update the notification later on.
				mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	    }

	
}
