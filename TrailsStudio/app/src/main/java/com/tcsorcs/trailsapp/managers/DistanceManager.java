package com.tcsorcs.trailsapp.managers;

import java.util.ArrayList;
import java.util.Calendar;

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

public class DistanceManager {
    // boolean marks if we've scanned these locations/assumed we've passed these
    // listed in this order: ExceEnt, L21, L20, L18, DepeEnt
    private Boolean[] markers = { false, false, false, false, false };
    private String firstCodeScanned;

    // temporary Database Glorious entries (in feet)
    private double ExceEnt_L21 = 380.0;
    private double L20_L21 = 349.0;
    private double L18_L20 = 741.0;
    private double DepeEnt_L18 = 66.0;

    private double totalDistance = 0.0;

    // timey-wimey stuff
    private Boolean started = false; // flag so start time isn't reset
    private long startTimeMillis = -1;

    /*
     * Not using this for anything yet - but using this will let us correctly
     * calculate time if using this app for > 24 hours
     */
    private int startDate = 0; // Not implemented

    public static DistanceManager getInstance() {
        return DistanceManager.instance;
    }

    public static DistanceManager instance = new DistanceManager();

    /*
     * Handles QR input
     */
    public void processQRCodes(String codeName) {
        if (!started) {
            started = true;
            startTimeMillis = System.currentTimeMillis();
            firstCodeScanned = codeName;
        }
        if (codeName.equals("ExceEnt")) {
            this.markers[0] = true;
            stupidPathFinder(0);
            DisplayManager.getInstance().updateTrailsMap(getPathSegments());
        } else if (codeName.equals("L21")) {
            this.markers[1] = true;
            stupidPathFinder(1);
            DisplayManager.getInstance().updateTrailsMap(getPathSegments());
        } else if (codeName.equals("L20")) {
            this.markers[2] = true;
            stupidPathFinder(2);
            DisplayManager.getInstance().updateTrailsMap(getPathSegments());
        } else if (codeName.equals("L18")) {
            this.markers[3] = true;
            stupidPathFinder(3);
            DisplayManager.getInstance().updateTrailsMap(getPathSegments());
        } else if (codeName.equals("DepeEnt")) {
            this.markers[4] = true;
            stupidPathFinder(4);
            DisplayManager.getInstance().updateTrailsMap(getPathSegments());
        }

        // awards achievement
        if (this.markers[0] && this.markers[1] && this.markers[2]
                && this.markers[3] && this.markers[4]) {
            // TODO add achievement call. this is currently called whenever
            // DepeEnt is scanned and finished zooming (in TouchImageView) for
            // the demo since we know that will be the
            // last scanned QR code
        }
    }

    /*
     * Returns sections of path walked First value is the first code scanned,
     * rest values are the segments on the path walked (not in the order they
     * were walked)
     */
    public ArrayList<String> getPathSegments() {
        ArrayList<String> pathList = new ArrayList<String>();

        pathList.add(firstCodeScanned);

        if (this.markers[0] && this.markers[1]) {
            pathList.add("ExceEnt_L21");
        }
        if (this.markers[1] && this.markers[2]) {
            pathList.add("L20_L21");
        }
        if (this.markers[2] && this.markers[3]) {
            pathList.add("L18_L20");
        }
        if (this.markers[3] && this.markers[4]) {
            pathList.add("DepeEnt_L18");
        }

        return pathList;
    }

    /*
     * Returns total distance walked note distance is calculated each time a
     * distanceQR code is scanned
     */
    public double getDistance() {
        return this.totalDistance;
    }

    /*
     * Returns total time on trail in seconds
     */
    public double getTimeOnTrail() {
        long currentTimeMillis = System.currentTimeMillis();
        long totalTimeMillis = currentTimeMillis - startTimeMillis;
        int totalTimeSeconds = (int)(totalTimeMillis/1000); //convert to seconds

        return totalTimeSeconds;
    }

    /*
     * Returns average pace since start in feet per minutes
     */
    public double getPace() {
        double seconds = getTimeOnTrail();
        double minutes = seconds/60;
        double distance = getDistance();
        double pace = distance / minutes;
        pace = Math.round(pace*1000)/1000;

        return pace;
    }

    // Checks off markers assuming we skipped some
    // default is start at ExeEnt
    private void stupidPathFinder(int currentScan) {
        int endPoint = 0;
        int i;

        // Stupid find entrance
        if (firstCodeScanned.equals("DepeEnt")
                || firstCodeScanned.equals("L18")) {
            endPoint = 4;
        }

        // fill in spaces between + marks off current
        if (endPoint < currentScan) {
            for (i = endPoint; i <= currentScan; i++) {
                this.markers[i] = true;
            }
        } else {
            for (i = currentScan; i <= endPoint; i++) {
                this.markers[i] = true;
            }
        }
        stupidDistanceCalc();
    }

    // calculates distance
    private void stupidDistanceCalc() {
        double calcDistance = 0.0;

        if (this.markers[0] && this.markers[1]) {
            calcDistance += ExceEnt_L21;
        }
        if (this.markers[1] && this.markers[2]) {
            calcDistance += L20_L21;
        }
        if (this.markers[2] && this.markers[3]) {
            calcDistance += L18_L20;
        }
        if (this.markers[3] && this.markers[4]) {
            calcDistance += DepeEnt_L18;
        }

        this.totalDistance = calcDistance;
    }

}