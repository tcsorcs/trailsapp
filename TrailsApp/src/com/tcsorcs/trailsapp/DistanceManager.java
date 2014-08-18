package com.tcsorcs.trailsapp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.widget.LinearLayout;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/* v0.0 - Demo only version
 *  Only Excellence trail codes available
 *  No database connection 
 *  Only works if adjacent markers are scanned in order: Exc -> L21 -> L18 -> Dep
 *  
 *  Doesn't display distance yet
 *  
 *  Need: marker names, distances, assistance with displaying items
 */

class DistanceManager {

	boolean seenExcEnt = false; //Excellence Entrance marker
	boolean seenL21 = false; //marker 1 on Excellence trail (? check this)
	boolean seenL18 = false; //marker 2 on Excellence trail (? check this)
	boolean seenDepeEnt = false; //Dependability Entrance marker
	List<String> pathList = new ArrayList<String>(); //list of all the markers scanned in scanned order
	double ExtoL21 = 100.0; //unknown
	double L21toL18 = 100.0; //unknown
	double L18toDep = 100.0; //unknown
	double totalDistance = 0.0; 
	
	public static DistanceManager getInstance() {
		return DistanceManager.instance;
	}
	static DistanceManager instance = new DistanceManager();

	/* 
	 * Handles QR input
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			String thisLocation = scanResult.getContents();
			
			//Don't know official names of markers yet - change when known
			if(c.endsWith("/marker/Excellence")){
				pathList.add("/marker/Excellence");
				seenExcEnt = true;
				//Need: distance traveled displayed
			}
			else if (c.endsWith("/marker/L21")){
				pathList.add("/marker/L21");
				seenL21 = true;
				if (seenExcEnt){
					totalDistance += ExtoL21;
					//Need: distance traveled displayed
				}
			}
			else if (c.endsWith("/marker/L18")){
				pathList.add("/marker/L18");
				seenL18 = true;
				if (seenL21){
					totalDistance += L21toL18;
					//Need: distance traveled displayed
				}
			}
			else if (c.endsWith("/marker/Dependability")){
				pathList.add("/marker/Dependability");
				seenDepeEnt = true;
				if (seenL18){
					totalDistance += L18toDep;
					//Need: distance traveled displayed
				}
			}
			else {	
				TextView t = new TextView(DisplayManager.getInstance().main_activity);
				l.removeAllViews();
				t.setText("Unknown barcode: "+scanResult.getContents());
				l.addView(t);		
			}


			if (seenExcEnt && seenL21 && seenL18 && seenDepeEnt){
				//Need: communicate to Achievements manager that the Executive Achievement is complete
			}
		}
	}
}
