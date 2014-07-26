package com.tcsorcs.trailsapp;

import com.google.zxing.integration.android.IntentIntegrator;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

class DisplayManager {
  public static DisplayManager getInstance() {
	  return DisplayManager.instance;
  }
  static DisplayManager instance = new DisplayManager();
  Activity main_activity = null;
  
  void setButtonCallbacks() {
      Button scanBarcodeButton = (Button) main_activity.findViewById(R.id.ScanBarcodeButton);
      scanBarcodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IntentIntegrator integrator = new IntentIntegrator(main_activity);
				integrator.initiateScan();
			}} );
  }
}
