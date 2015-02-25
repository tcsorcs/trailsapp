package com.tcsorcs.trailsapp.managers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.activites.AchievementActivity;
import com.tcsorcs.trailsapp.activites.GoogleMapsActivity;
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

//        //Google Maps Button
//        Button gmButton = (Button) main_activity
//                .findViewById(R.id.GoogleMapsButton);
//        gmButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Intent i = new Intent(main_activity,GoogleMapsActivity.class);
//                main_activity.startActivity(i);
//
//            }
//        });


        //Scan QR Code Button
        Button scanBarcodeButton = (Button) main_activity
                .findViewById(R.id.ScanBarcodeButton);
        scanBarcodeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //old bar codescanner app
                // IntentIntegrator integrator = new IntentIntegrator(
                //        main_activity);
                // integrator.initiateScan();

                //package name for QR Code Reader App
                final String appPackageName = "me.scan.android.client";

                //check if qRCodeReader is installed
                if (isPackageInstalled(appPackageName, main_activity)) {
                    //if it is, go to it so we can scan a qr code
                    //format of url is given by QR Code Reader Documentaiton
                    //https://scan.me/
                    //https://scan.me/help#/article/1363347
                    String url = "scan://scan?callback=com.tcsorcs.trailsapp://tcstrails";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    main_activity.startActivity(i);

                } else {
                    //if app is not installed, go to installation page in market
                    try {
                        main_activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        //play store isn't installed on device, go to app in a browser
                        main_activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }


            }
        });
    }

    /**
     * @param achievementId - string value of achievement name "Executive" for prototype
     */
    public void notifyAcheivement(String achievementId) {
        if (achievementId.equals("Executive")) {
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
    public void stopGatheringTime() {

        //cancel thread collecting time
        if (timeOnTrailsViewRunnable != null) {
            timeOnTrail.removeCallbacks(timeOnTrailsViewRunnable);
            timeOnTrailsViewRunnable = null;

        }

    }

    /**
     * if onTrailsService is running, start updating textviews for pace, time,
     * distance
     */
    public void beginGatheringTime() {

        if (onTrailsServiceRunning) {
            timeOnTrail = (TextView) main_activity
                    .findViewById(R.id.time_on_trail_text);
            totalDistance = (TextView) main_activity
                    .findViewById(R.id.distance_text);
            currentPace = (TextView) main_activity.findViewById(R.id.pace_text);

            // TIME ON TRAILS RUNNABLE

            //start thread to begin gathering time

            timeOnTrailsViewRunnable = new Runnable() {
                @Override
                public void run() {


                    String convertedTime = convertSecondsToTimeString(DistanceManager.getInstance()
                            .getTimeOnTrail());

                    timeOnTrail.setText(convertedTime);

                    timeOnTrail.postDelayed(timeOnTrailsViewRunnable, 1000);
                }
            };
            // add to message queue
            timeOnTrail.post(timeOnTrailsViewRunnable);

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
    public void initializeMapView() {
        mapPanView = (TouchImageView) main_activity
                .findViewById(R.id.MapPanView);
        // set max zoom a user can zoom into the map when double tapping or
        // pinching
        mapPanView.setMaxZoom(10);

        //first map has no points marked
        mapPanView.setImageResource(R.drawable.trails_nomarkup);
    }

    /**
     * updates the map view on the home activity given the values in the passed
     * arraylist (ExceEnt, ExceEnt_L21...etc)
     *
     * @param currentPathSegments ArrayList of path segments from distance manager so we know
     *                            what map points to display on the homeactivity
     */
    public void updateTrailsMap(ArrayList<String> currentPathSegments) {
        // //currentPathSegments=DistanceManager.getInstance().getPathSegments();
        int numberOfPaths = currentPathSegments.size();

        if (!onTrailsServiceRunning) {
            // flag service is started/begin started
            onTrailsServiceRunning = true;
            // begin populating distance, pace, time on trails
            beginGatheringTime();
            // start the onTrailsService
            Intent intent = new Intent(main_activity, OnTrailsService.class);
            main_activity.startService(intent);
        }

        if (currentPathSegments.size() == 1) {
            //show labels on first scan
            TextView timeLabel = (TextView) main_activity
                    .findViewById(R.id.TimeLabelText);
            TextView distanceLabel = (TextView) main_activity
                    .findViewById(R.id.DistanceLabelText);
            TextView paceLabel = (TextView) main_activity
                    .findViewById(R.id.PaceLabelText);

            timeLabel.setVisibility(View.VISIBLE);
            distanceLabel.setVisibility(View.VISIBLE);
            paceLabel.setVisibility(View.VISIBLE);

            totalDistance.setText(DistanceManager.getInstance().getDistance()
                    + " " + DISTANCE_UNITS);
            currentPace.setText("on next QR code! Better hurry :)");
        } else {
            // update both pace and time for all other scenarios
            totalDistance.setText(DistanceManager.getInstance().getDistance()
                    + " " + DISTANCE_UNITS);
            currentPace.setText(DistanceManager.getInstance().getPace() + " "
                    + PACE_UNITS);
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

        float targetZoom = 6;

        DoubleTapZoom doubleTap = null;

         //points for each location on map
        float ExceEntBitmapX = 1698f;
        float ExceEntBitmapY = 1805f;

        float ExceEnt_L21X = 1565f;
        float ExceEnt_L21Y = 1922f;

        float L20_L21X = 1501f;
        float L20_L21Y = 1790f;

        float L18_L20X = 1670f;
        float L18_L20Y = 1572f;

        float DepeEnt_L18X = 1735f;
        float DepeEnt_L18Y = 1633f;

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

    public boolean isOnTrailsServiceRunning() {
        return onTrailsServiceRunning;
    }

    //todo formating of time when seconds/minutes/hours are not length of 2
    public String convertSecondsToTimeString(double totalSecondsDouble) {
        String resultString = "";
        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int totalSeconds = (int) Math.round(totalSecondsDouble);
        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        if (hours > 0) {
            resultString = hours + ":" + minutes + ":" + seconds;
        } else if (minutes > 0) {
            //single digit minutes
            resultString = minutes + ":" + seconds;
        } else {
            resultString = "00" + ":" + seconds;
        }


        return resultString;
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
