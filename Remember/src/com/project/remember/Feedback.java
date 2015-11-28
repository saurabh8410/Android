package com.project.remember;

import com.project.remember.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends Fragment {

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_feedback, container, false);
		Button btnSend = (Button) view.findViewById(R.id.btnSend);
		final EditText txtEmailId = (EditText) view
				.findViewById(R.id.txtEmailId), txtMsg = (EditText) view
				.findViewById(R.id.txtMsg);
		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (txtMsg.getText().toString().equals("")
						|| txtEmailId.getText().toString().equals("")) {
					Toast.makeText(view.getContext(),
							"Please fill all information first",
							Toast.LENGTH_LONG).show();
				} else {
					Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							"saurabhsharma111@ymail.com");
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"Feedback");
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							txtMsg.getText().toString() + "  "
									+ txtEmailId.getText().toString());
					startActivity(emailIntent);
				}
				Fragment f = new HomeFragment();
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, f).commit();
			}
		});
		return view;
	}

}
