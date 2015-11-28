package com.project.remember;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class RebootReceiver extends BroadcastReceiver {
	String row_id = "";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			AlarmReceiver alarm=new AlarmReceiver();
			alarm.setAlarmForTable1(context);
			alarm.setAlarmForTable2(context);
		}
	}
}