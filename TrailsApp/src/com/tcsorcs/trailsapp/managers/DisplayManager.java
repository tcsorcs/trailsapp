package com.tcsorcs.trailsapp.managers;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.activites.AchievementActivity;
import com.tcsorcs.trailsapp.mapview.TouchImageView;
import com.tcsorcs.trailsapp.mapview.TouchImageView.DoubleTapZoom;
import com.tcsorcs.trailsapp.services.OnTrailsService;

public class DisplayManager {

	public static DisplayManager getInstance() {
		return DisplayManager.instance;
	}

	static DisplayManager instance = new DisplayManager();
	public Activity main_activity = null;

	// used for comparing values in array from DistanceManager getSegmentPaths()
	public static final String ExceEnt = "ExceEnt";
	public static final String ExceEnt_L21 = "ExceEnt_L21";
	public static final String L20_L21 = "L20_L21";
	public static final String L18_L20 = "L18_L20";
	public static final String DepeEnt_L18 = "DepeEnt_L18";

	// unit labels for textview Pace, Distance Time on Trails
	private final String DISTANCE_UNITS = "feet";
	private final String TIME_UNITS = "minutes";
	private final String PACE_UNITS = "ft/min";

	private boolean onTrailsServiceRunning = false; // boolean to keep track if

	// used in beginUpdatingDistancePaceTime() for updating textviews while a
	// person is on the trails
	private TextView timeOnTrail;
	private TextView totalDistance;
	private TextView currentPace;

	// runnables for updating pace, time on trails, distance at specific
	// intervals on separate threads
	private Runnable timeOnTrailsViewRunnable = null;
	private Runnable paceViewRunnable = null;
	private Runnable distanceViewRunnable = null;

	// the map view on the home activity
	private TouchImageView mapPanView;

	/**
	 * set home activity button onClicks
	 */
	public void setButtonCallbacks() {
		Button scanBarcodeButton = (Button) main_activity
				.findViewById(R.id.ScanBarcodeButton);
		scanBarcodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IntentIntegrator integrator = new IntentIntegrator(
						main_activity);
				integrator.initiateScan();
			}
		});

		Button exitButton = (Button) main_activity
				.findViewById(R.id.ExitButton);
		exitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new AlertDialog.Builder(main_activity)
						.setMessage("Are you sure you want to exit?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										// stop service if it is running
										stopOnTrailsService();

										stopGatheringPaceTimeDistance();
										// ((BitmapDrawable)mapPanView.getDrawable()).getBitmap().recycle();

										// exit our trails main activity
										// application
										main_activity.finish();
									}
								}).setNegativeButton("No", null).show();

			}
		});
	}

	/**
	 * @param acheivmentId
	 *            - string value of achievement name "Executive" for prototype
	 */
	public void notifyAcheivement(String acheivmentId) {
		if (acheivmentId.equals("Executive")) {
			// start Achievement activity which shows Executive achievment for
			// demo
			Intent intent = new Intent(main_activity, AchievementActivity.class);
			main_activity.startActivity(intent);
		}
	}

	/**
	 * if currently gathering pace, time, distance from Display manager- remove
	 * runnables that are updating these to save system resources when our
	 * activity is not in view
	 */
	public void stopGatheringPaceTimeDistance() {

		if (timeOnTrailsViewRunnable != null) {
			timeOnTrail.removeCallbacks(timeOnTrailsViewRunnable);
			timeOnTrailsViewRunnable = null;

		}
		if (paceViewRunnable != null) {
			currentPace.removeCallbacks(paceViewRunnable);
			paceViewRunnable = null;
		}
		if (distanceViewRunnable != null) {
			totalDistance.removeCallbacks(distanceViewRunnable);
			distanceViewRunnable = null;

		}

	}

	/**
	 * if onTrailsService is running, start updating textviews for pace, time,
	 * distance
	 */
	public void beginGatheringPaceTimeDistance() {

		if (onTrailsServiceRunning) {
			timeOnTrail = (TextView) main_activity
					.findViewById(R.id.time_on_trail_text);
			totalDistance = (TextView) main_activity
					.findViewById(R.id.distance_text);
			currentPace = (TextView) main_activity.findViewById(R.id.pace_text);

			// TIME ON TRAILS RUNNABLE
			timeOnTrailsViewRunnable = new Runnable() {
				@Override
				public void run() {

					// TODO getTimeOnTrail() from DistanceManager
					/*
					 * timeOnTrail.setText(Long.toString(System
					 * .currentTimeMillis()) + " " + TIME_UNITS);
					 */

					timeOnTrail.setText(DistanceManager.getInstance()
							.getTimeOnTrail() + " " + TIME_UNITS);

					timeOnTrail.postDelayed(timeOnTrailsViewRunnable, 1000);
				}
			};
			// add to message queue
			timeOnTrail.post(timeOnTrailsViewRunnable);

			// PACE
			paceViewRunnable = new Runnable() {
				@Override
				public void run() {
					// TODO getPace() from DistanceManager
					/*
					 * currentPace.setText(Long.toString(System
					 * .currentTimeMillis()) + " " + PACE_UNITS);
					 */

					currentPace.setText(DistanceManager.getInstance().getPace()
							+ " " + PACE_UNITS);

					currentPace.postDelayed(paceViewRunnable, 1000);
				}
			};
			// add to message queue
			currentPace.post(paceViewRunnable);

			// DISTANCE

			distanceViewRunnable = new Runnable() {
				@Override
				public void run() {
					// TODO getDistance() from DistanceManager
					/*
					 * totalDistance.setText(Long.toString(System
					 * .currentTimeMillis()) + " " + DISTANCE_UNITS);
					 */

					totalDistance.setText(DistanceManager.getInstance()
							.getDistance() + " " + DISTANCE_UNITS);

					totalDistance.postDelayed(distanceViewRunnable, 1000);

				}
			};
			// add to message queue
			totalDistance.post(distanceViewRunnable);

		}

	}

	/**
	 * stops the notification and service since user is no longer on a trail
	 */
	public void stopOnTrailsService() {

		onTrailsServiceRunning = false;
		// stop service if it is running / if app is killed from task manager
		// this makes sure that our service is killed also upon killing our app
		main_activity.stopService(new Intent(main_activity,
				OnTrailsService.class));

		// TODO need to research why distance manager holds values after exiting
		// app and restarting so we do not need to do this. something isn't
		// getting released
		// reset distance manager for next trails start
		DistanceManager.instance = new DistanceManager();

	}

	/**
	 * sets the initial map view for the home activity before any trail qr is
	 * scanned
	 */
	public void initiliaizeMapView() {
		mapPanView = (TouchImageView) main_activity
				.findViewById(R.id.MapPanView);
		// set max zoom a user can zoom into the map when double tapping or
		// pinching
		mapPanView.setMaxZoom(10);

		mapPanView.setImageResource(R.drawable.trails_nomarkup);
	}

	/**
	 * updates the map view on the home activity given the values in the passed
	 * arraylist (ExceEnt, ExceEnt_L21...etc)
	 * 
	 * @param currentPathSegments
	 *            ArrayList of path segments from distance manager so we know
	 *            what map points to display on the homeactivity
	 */
	public void updateTrailsMap(ArrayList<String> currentPathSegments) {
		// //currentPathSegments=DistanceManager.getInstance().getPathSegments();
		int numberOfPaths = currentPathSegments.size();

		// start notification service
		if (!onTrailsServiceRunning) {
			// flag service is started/begin started
			onTrailsServiceRunning = true;
			// begin populating distance, pace, time on trails
			beginGatheringPaceTimeDistance();

			// start the onTrailsService
			Intent intent = new Intent(main_activity, OnTrailsService.class);
			main_activity.startService(intent);
		}

		boolean containsExceEnt = false;
		boolean containsExceEnt_L21 = false;
		boolean containsL20_L21 = false;
		boolean containsL18_L20 = false;
		boolean containsDepeEnt_L18 = false;

		containsExceEnt = currentPathSegments.contains(ExceEnt);
		containsExceEnt_L21 = currentPathSegments.contains(ExceEnt_L21);
		containsL20_L21 = currentPathSegments.contains(L20_L21);
		containsL18_L20 = currentPathSegments.contains(L18_L20);
		containsDepeEnt_L18 = currentPathSegments.contains(DepeEnt_L18);

		// for zooming to location

		float targetZoom = mapPanView.getMaxZoom();

		DoubleTapZoom doubleTap = null;

		// TODO
		// coordinates for each location on map to zoom in to
		// this is only wokring on Dave's Note 4 at the moment
		// more research needed :D
		float ExceEntBitmapX = 2269f;
		float ExceEntBitmapY = 2428f;

		float ExceEnt_L21X = 2049f;
		float ExceEnt_L21Y = 2593f;

		float L20_L21X = 1989;
		float L20_L21Y = 2413;

		float L18_L20X = 2283f;
		float L18_L20Y = 2133f;

		float DepeEnt_L18X = 2336f;
		float DepeEnt_L18Y = 2193f;

		// first Scan
		if (numberOfPaths == 1 && containsExceEnt) {
			// scanned ExceEnt
			doubleTap = mapPanView.new DoubleTapZoom(targetZoom, 0, 0, false,
					ExceEntBitmapX, ExceEntBitmapY, R.drawable.exceent,
					mapPanView);
			mapPanView.compatPostOnAnimation(doubleTap);

		}
		// 2 segment
		else if (numberOfPaths == 2 && containsExceEnt && containsExceEnt_L21) {
			//
			doubleTap = mapPanView.new DoubleTapZoom(targetZoom, 0, 0, false,
					ExceEnt_L21X, ExceEnt_L21Y, R.drawable.exceent_l21,
					mapPanView);
			mapPanView.compatPostOnAnimation(doubleTap);

		}

		// 3 segments
		else if (numberOfPaths == 3 && containsExceEnt && containsExceEnt_L21
				&& containsL20_L21) {

			doubleTap = mapPanView.new DoubleTapZoom(targetZoom, 0, 0, false,
					L20_L21X, L20_L21Y, R.drawable.exceent_l21_l20_l21,
					mapPanView);
			mapPanView.compatPostOnAnimation(doubleTap);

		}

		// 4 segments
		else if (numberOfPaths == 4 && containsExceEnt && containsExceEnt_L21
				&& containsL20_L21 && containsL18_L20) {

			doubleTap = mapPanView.new DoubleTapZoom(targetZoom, 0, 0, false,
					L18_L20X, L18_L20Y, R.drawable.exceent_l21_l20_l21_l18_l20,
					mapPanView);
			mapPanView.compatPostOnAnimation(doubleTap);

		}
		// 5 segments
		else if (numberOfPaths == 5 && containsExceEnt && containsExceEnt_L21
				&& containsL20_L21 && containsL18_L20 && containsDepeEnt_L18) {

			doubleTap = mapPanView.new DoubleTapZoom(targetZoom, 0, 0, false,
					DepeEnt_L18X, DepeEnt_L18Y,
					R.drawable.exceent_l21_l20_l21_l18_l20_depeent_l18,
					mapPanView);
			mapPanView.compatPostOnAnimation(doubleTap);

		}
	}

}
