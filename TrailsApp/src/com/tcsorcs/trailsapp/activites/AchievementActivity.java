package com.tcsorcs.trailsapp.activites;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.managers.SoundManager;

public class AchievementActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// for prototype only show one achievment (hard coded into
		// achievement_view layout has image hard coded into imageview
		setContentView(R.layout.achievement_view);

		// TODO achievement activity will receive information from the intent
		// regarding what achievement id it should pull from DAO and then can
		// decide on sound clip etc..
		SoundManager.getInstance().playSound("hooray");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.MainLinearLayout) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// finish this achievement activity when pressing back button and return
		// to main activity
		finish();
	}

}
