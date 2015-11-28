package com.project.remember;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public Fragment getItem(int index) {
		 
	        switch (index) {
	        case 0:
	            return new TabBirthdays();
	        case 1: 
	        	 return new TabAnivarsary();
	        case 2:
	        	return new TabMedicine();
	        case 3:
	        	return new TabTasks();
	        case 4:
	        	return new TabMeetings();
	        }
	 
	        return null;
	    }
	 
	    @Override
	    public int getCount() {
	        // get item count - equal to number of tabs
	        return 5;
	    }
	
}
