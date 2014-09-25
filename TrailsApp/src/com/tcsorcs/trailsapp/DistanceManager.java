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
	//listed in this order: ExceEnt, L21, L20, L18, DepeEnt
	Boolean [] markers = {false, false, false, false, false};

	//temporary Database Glorious entries (in feet)
	double ExceEnt_L21 = 380.0;
	double L20_L21 = 349.0; 
	double L18_L20 = 741.0; 
	double DepeEnt_L18 = 66.0; 
	
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
			this.markers[0] = true;
			stupidPathFinder(0);
			//Distance traveled displayed?
		}
		else if(codeName.equals("L21")){
			this.markers[1] = true;
			stupidPathFinder(1);
			//Distance traveled displayed?
		}
		else if(codeName.equals("L20")){
			this.markers[2] = true;
			stupidPathFinder(2);
			//Distance traveled displayed?
		}
		else if(codeName.equals("L18")){
			this.markers[3] = true;
			stupidPathFinder(3);
			//Distance traveled displayed?
		}
		else if(codeName.equals("DepeEnt")){
			this.markers[4] = true;
			stupidPathFinder(4);
			//Distance traveled displayed?
		}

		//awards achievement
		if (this.markers[0] && this.markers[1] && this.markers[2] && this.markers[3] && this.markers[4]){
			AchievementManager.AwardAchievement("Executive Achievement", "You've walked the Executive trail!");
		}
	}
	
	/*
	 * Returns sections of path walked
	 */
	public ArrayList<String> getPathSegments(){
		ArrayList<String> pathList = new ArrayList<String>();
		
		if (this.markers[0] && this.markers[1]){
			pathList.add("ExceEnt_L21");
		}
		if (this.markers[1] && this.markers[2]){
			pathList.add("L20_L21");
		}
		if (this.markers[2] && this.markers[3]){
			pathList.add("L18_L20");
		}
		if (this.markers[3] && this.markers[4]){
			pathList.add("DepeEnt_L18");
		}
		
		return pathList;
	}
	
	/*
	 * Returns total distance walked
	 */
	public double getDistance(){
		return this.totalDistance;
	}
	
	//Checks off markers assuming we skipped some
	private void stupidPathFinder(int currentScan){
		int endPoint = 4;
		int i;
	
		//find the entrance
		for (i = 0; i < 5; i++){
			if (this.markers[i] && i < currentScan){
				endPoint = 0;
			}
			else if (this.markers[i] && i > currentScan){
				endPoint = 4;
			}
		}
		
		//fill in spaces between + marks off current
		if (endPoint < currentScan){
			for (i = endPoint; i <= currentScan; i++){
				this.markers[i] = true;
			}
		}
		else {
			for (i = currentScan; i <= endPoint; i++){
				this.markers[i]= true;
			}
		}
		stupidDistanceCalc();
	}
	
	//calculates distance
	private void stupidDistanceCalc(){
		double calcDistance = 0.0;
		
		if (this.markers[0] && this.markers[1]){
			calcDistance+=ExceEnt_L21;
		}
		if (this.markers[1] && this.markers[2]){
			calcDistance+=L20_L21;
		}
		if (this.markers[2] && this.markers[3]){
			calcDistance+=L18_L20;
		}
		if (this.markers[3] && this.markers[4]){
			calcDistance+=DepeEnt_L18;
		}
		
		this.totalDistance = calcDistance;
	}
	
}

