package com.tcsorcs.trailsapp.managers;

import java.util.ArrayList;
import java.util.Calendar;

/* v1.0 - Full version
 *  
 *  Currently contains Demo version code - need to update
 *  
 */
 /*
 *   Stuff Distance manager will need for 1 session
  *  >Each distance code scanned needs this information stored: 
  *    Name (String), order scanned (int), time scanned (long)
  *  >Need distances between adjacent codes stored as:
  *    Code Name1 (String), Code Name2 (String), distance (double)
  *   It would be super dooper great if the order each code is listed is consistent - preferably 
  *     alphabetically or some other easy way to sort.
  *  
  *  Stuff for stats recording (expanded from above)
  *  Pace Stats Recording:
  *    Code Name1 (String), Code Name2 (String), distance (double), currentPace (double), averagePace (double)
  *  Is there a way to store an array/mutable sized storage container in the DB? If so, then just use that
  *    to record all paces per section of the trail, and calculate average from that data when we need to.
  *    If so, the stats recording/entry for each path section could be:
  *    Code Name1 (String), Code Name2 (String), distance (double), currentPace (double), pastPaces (array of doubles)
  *
  *  Routes Stats Recording:
  *  Basic idea is we have a trail identifier (more later), a tally counting the times walked, and a 
  *    container to store all paces (as described in Pace Stats Recording)
  *    ID, timesWalked (int), pastPaces? (array of doubles)
  *  If this identifier is a nickname (could be nickname or id) of all the logical trail loops we could 
  *    make an entry for each in the DB before the app is ever run. 
  *    - would need a conversion table from segments to trail name
  *    = would still need the ability to add new "loops" on the fly (if you only scanned part of a loop)
  *    + more human readable for debugging
  *    +/- we know we have (the majority of) the space we'd need, but there would be wasted space
  *    + don't care about the start/end
  *    ? if a walk consists of multiple loops, do we tally each sub loop, or make a new entry for the
  *       total loop
  *    ? may not need pastPaces section if we make something grab paces for each segment of each trail
  *  If this identifier is a container (list/array etc like for pastPaces - assuming this is possible) we
  *    could dynamically add new trail loops as the user uses them
  *    - each trail loop needs to have a standard start/end, or there will be duplicates
  *    +/- DB increases as needed, but total space of app will vary more
  *    - will need a smarter search algorithm to decide which trail we've just walked
  *    + do not need a pastPaces column (can calculate it as needed based on pace for each segment)
  *    -/? related to ? from above, if segments are not broken down, may have many long trails only walked once
  *
  *  Or could only store info that we need for an achievement (ex, only tally the Kurtis F Trail walks)
  *    would simplify the problems above
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

    /*****
     * stupidPathFinder and stupidDistanceCalc will be replaces with smarterPathFinder soon.
     */
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

    /*A smarter path finder that handles the whole trail
     * handles skipped points, crossing the road ...
     */

    private void smarterPathFinder(int currentScan){

    }
}