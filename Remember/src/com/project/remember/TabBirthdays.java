package com.project.remember;
import com.project.remember.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class TabBirthdays extends Fragment implements OnItemClickListener {
	
AlarmReceiver alarm=new AlarmReceiver();
	View rootView;
	 ListView lv_events;
	 DBClass db;
	 Cursor c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.tab_birthdays, container, false);
    	db=new DBClass(this.getActivity());
    	show_data();
		return rootView;
    }
    
    @Override
   	public void onResume() {
   		// TODO Auto-generated method stub
   		super.onResume();
   		show_data();
   	}
    
    private void show_data(){
    	String from[]=new String[]{db.FIELD_ID,db.FIELD_NAME,db.FIELD_INTEREST,db.FIELD_ADATE,db.FIELD_ATIME};
    	int to[]=new int[]{R.id.tv_id,R.id.tv_title,R.id.tv_interest,R.id.tv_date,R.id.tv_time};
    	lv_events=(ListView)rootView.findViewById(R.id.bdlv_events);
    	try{
    	c=db.queryForBd();
    	@SuppressWarnings("deprecation")
		SimpleCursorAdapter cursorAdapter=new SimpleCursorAdapter(this.getActivity(),R.layout.list_row,c,from,to);
    	lv_events.setAdapter(cursorAdapter);
    	lv_events.setOnItemClickListener(this);
    	db.close();
    	}
    	catch(Exception e){e.getStackTrace();
    	}
	
    }
    @SuppressWarnings("deprecation")
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		// TODO Auto-generated method stub
    	 
    	final String id = ((TextView)view.findViewById(R.id.tv_id)).getText().toString();
    	
    	AlertDialog alert=new AlertDialog.Builder(this.getActivity()).create();
		alert.setTitle("Options");
		alert.setIcon(R.drawable.ic_home);
		alert.setMessage("What do you want to do?");
		alert.setButton2("Edit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Bundle b=new Bundle();
				b.putString("row_id", id);
				Intent i=new Intent(rootView.getContext(),Birthday.class);
				i.putExtras(b);
				rootView.getContext().startActivity(i);
			}
		});
		alert.setButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				alarm.removePendingIntents(rootView.getContext());
				delete(id);
				alarm.setAlarmForTable1(rootView.getContext());
				Toast.makeText(getActivity(), "Event Deleted", Toast.LENGTH_SHORT).show();
			}
		});
		alert.show();
	}  
    
    private void delete(String id){
    	DBClass db=new DBClass(this.getActivity());
    	try {
			db.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	db.deleteData(id);
    	db.close();
    	show_data();
    }
}

