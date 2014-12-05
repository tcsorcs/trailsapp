package com.tcsorcs.trailsapp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); //SH? layout doesn't exist?
		DisplayManager.getInstance().main_activity = this;
		DisplayManager.getInstance().setButtonCallbacks();
	}


public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			String c = scanResult.getContents();
			LinearLayout l = (LinearLayout) findViewById(R.id.MainLinearLayout);
			InputManager.getInstance().inputQRC(c, l);
			/*
			if(c.endsWith("/achievements/executive")) {
				ImageView i = new ImageView(DisplayManager.getInstance().main_activity);
				i.setImageDrawable(getResources().getDrawable(R.drawable.achievement));
				l.removeAllViews();
				l.addView(i);
			}
			else {
				TextView t = new TextView(DisplayManager.getInstance().main_activity);
				l.removeAllViews();
				t.setText("Unknown barcode: "+scanResult.getContents());
				l.addView(t);
			}*/
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
}
