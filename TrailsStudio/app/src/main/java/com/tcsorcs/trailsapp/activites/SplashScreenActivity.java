package com.tcsorcs.trailsapp.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.tcsorcs.trailsapp.R;

public class SplashScreenActivity extends Activity {
	private static int SPLASH_TIME_OUT = 3000; // time to keep splash screen
	private Activity splash_activity; // visible

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		splash_activity = this;
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// This method will be executed once the timer is over

				// go to main activity, this splash screen activity is destroyed
				// in the onDestroy method below
				Intent i = new Intent(splash_activity, MainTrailsActivity.class);
				splash_activity.startActivity(i);

			}
		}, SPLASH_TIME_OUT);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.MainLinearLayout) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onBackPressed() {
		// if user presses back button while on splash screen activity return to
		// android desktop
		// TODO or do we want to exit?
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}

	@Override
	public void onStop() {
		super.onStop();
		// destroy splash screen activity
		((ImageView) this.findViewById(R.id.splashScreenImage))
				.setImageBitmap(null);
		finish();
	}

}
