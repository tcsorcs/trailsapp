package com.tcsorcs.trailsapp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.widget.LinearLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

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
	
	//timey-wimey stuff
	Boolean started = false; //flag so start time isn't reset
	Calendar aCalendar = Calendar.getInstance(); //to get system time
	int startHour = -1;
	int startMinute = -1;
	int startTimeInMinutes = -1;
	
	/*
	 * Not using this for anything yet - but using this will let us correctly calculate
	 * time if using this app for > 24 hours
	 */
	int startDate = 0; //counts back, so negative values are valid
	
	
		
	public static DistanceManager getInstance() {
		return DistanceManager.instance;
	}
	
	static DistanceManager instance = new DistanceManager();

	/* 
	 * Handles QR input
	 */
	public void processQRCodes(String codeName) {
		if (!started){
			started = true;
			startHour = aCalendar.get(Calendar.HOUR_OF_DAY);
			startMinute = aCalendar.get(Calendar.MINUTE);
			startTimeInMinutes = (startHour * 60) + startMinute;
			startDate = aCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		}
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
	 *    note distance is calculated each time a distanceQR code is scanned
	 */
	public double getDistance(){
		return this.totalDistance;
	}
	
	/*
	 * Returns total time on trail in minutes
	 * 
	 */
	public int getTimeOnTrail(){
		int currentHour = aCalendar.get(Calendar.HOUR_OF_DAY);
		int currentMinute = aCalendar.get(Calendar.MINUTE);
		int currentTimeInMinutes = (currentHour * 60) + currentMinute;
		
		int totalMinutes = currentTimeInMinutes - startTimeInMinutes;
				
		if (totalMinutes < 0){
			totalMinutes = 1440 + totalMinutes;
		} 
		
		return totalMinutes;
	}
	
	/*
	 * Returns average pace since start in feet per minutes
	 */
	public Double getPace(){
		int minutes = getTimeOnTrail();
		Double distance = getDistance();
		Double pace = distance/minutes;
		
		return pace;
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

