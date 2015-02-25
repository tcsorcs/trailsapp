package com.tcsorcs.trailsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tcsorcs.trailsapp.managers.DisplayManager;
import com.tcsorcs.trailsapp.managers.DistanceManager;
import com.tcsorcs.trailsapp.managers.GeneralManager;
import com.tcsorcs.trailsapp.managers.InputManager;
import com.tcsorcs.trailsapp.mapview.TouchImageView;

public class MainTrailsActivity extends Activity {

	private boolean trailQRScanFound = false;
	private Handler qrCodeHandler = new Handler();
	String trailQRCodeContents = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orc_home);

		GeneralManager.getInstance().main_activity = this;
		DisplayManager.getInstance().main_activity = GeneralManager
				.getInstance().main_activity;

		// set onClicks for Buttons
		DisplayManager.getInstance().setButtonCallbacks();
		// set initial map image/zoom limit to be displayed on the home activity
		DisplayManager.getInstance().initiliaizeMapView();

		// TODO when exiting app, restarting app, distancemanager remembers it's
		// arraylist are we exiting app correctly? for now just assigning new
		// when we onCreate
		DistanceManager.instance = new DistanceManager();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		// look to see if we are returning from qr code scanner
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			String c = scanResult.getContents();
			if (c != null && c.length() > 0) {
				trailQRScanFound = true;
				trailQRCodeContents = new String(c);
			}
		}

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
		// go to home screen when the back button is pressed
		// TODO do we want to exit when back button is pressed?
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

		// whenever our display is hidden, stop updating textviews pace,
		// distance, time on trails
		DisplayManager.getInstance().stopGatheringPaceTimeDistance();
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		// whenever our main display is brought back to the foreground, continue
		// updating textviews pace, distance, time on trails
		DisplayManager.getInstance().beginGatheringPaceTimeDistance();

		// send qr contents to inputmanager if found, pausing 3 seconds
		// to allow for mapview to load and show transition to new location
		// without the 3 second delay it just goes directly to the location
		if (trailQRScanFound) {
			// reset flag for future scans
			trailQRScanFound = false;
			// pass qr code contents to input manager
			qrCodeHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					InputManager.getInstance().inputQRC(trailQRCodeContents);

				}
			}, 3000);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// end notification service that shows trails app is running
		DisplayManager.getInstance().stopOnTrailsService();

		TouchImageView mapView = (TouchImageView) this
				.findViewById(R.id.MapPanView);

		// setting the mapview bitmap to null gets rid of OutOfMemory exception
		// when running the app multiple times (exiting and reopening)
		// TODO more research on handling switching images
		mapView.setImageBitmap(null);

	}

}
