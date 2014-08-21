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
 *  Only works if codes are scanned in this order, either forward or backwards ExceEnt <-> L21 <-> L20 <-> L18 <-> DepeEnt
 *   (no running back and forth scanning codes for bonus points ;)
 *  
 *  Doesn't display distance yet
 *  
 *  Need: assistance with displaying items
 */

class DistanceManager {
	//boolean marks if we've scanned these locations/assumed we've passed these
	boolean ExceEnt = false;
	boolean L21 = false; 
	boolean L20 = false;
	boolean L18 = false;
	boolean DepeEnt = false;

	//temporary Database Glorious entries
	double ExceEnt_L21 = 380.0;
	double L20_L21 = 349.0; 
	double L18_L20 = 741.0; 
	double DepeEnt_L18 = 66.0; 
	
	List<String> pathList = new ArrayList<String>(); //list of all the markers scanned in scanned order
	double totalDistance = 0.0; 
	
	public static DistanceManager getInstance() {
		return DistanceManager.instance;
	}
	static DistanceManager instance = new DistanceManager();

	/* 
	 * Handles QR input
	 */
	public void processQRCodes(String codeName) {
		if(codeName.equals("ExceEnt")){
			ExceEnt = true;
			stupidPathFinder();
			pathList.add("ExceEnt");
			//Distance traveled displayed?
		}
		else if(codeName.equals("L21")){
			L21 = true;
			stupidPathFinder();
			pathList.add("L21");
			//Distance traveled displayed?
		}
		else if(codeName.equals("L20")){
			L20 = true;
			stupidPathFinder();
			pathList.add("L20");
			//Distance traveled displayed?
		}
		else if(codeName.equals("L18")){
			L18 = true;
			stupidPathFinder();
			pathList.add("L18");
			//Distance traveled displayed?
		}
		else if(codeName.equals("DepeEnt")){
			DepeEnt = true;
			stupidPathFinder();
			pathList.add("DepeEnt");
			//Distance traveled displayed?
		}

		if (ExceEnt && L21 && L20 && L18 && DepeEnt){
			//Need: communicate to Achievements manager that the Executive Achievement is complete
		}
	}
	
	//Checks off markers assuming we skipped some
	private void stupidPathFinder(){
		//is being stupid now. 
		
	}
}

