package com.tcsorcs.trailsapp.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Stack;
import java.util.TreeSet;

import com.tcsorcs.trailsapp.helpers.Location;
import com.tcsorcs.trailsapp.helpers.Segment;
import com.tcsorcs.trailsapp.helpers.DummyDatabaseHelper; //temporary until database helper is up

    /* v1.0 - Full version
     *
     *  Currently NOT FUNCTIONAL - needs actual hooks to connect to database and hooks for display manager to be implemented
     *  DNE = does not exist
     *  Updated 3/9/2015
     */
     /* Below are ideas, some outdated. Will clean up later.
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

/*
 * Assumptions made about classes that don't exist:
 * Segment class generated from info returned from the DB
 * need method: ArrayList<Segment> getSegmentsWithPoint(String pointA) - returns all segments with pointA
 * need method: boolean segmentHasPoints(String pointA, String pointB) - returns true if segment contains both points
 * Note that in both methods, "pointA" or "pointB" can be either "pointX" or "pointY"
 * Segment class {
 *    String pointX;
 *    String pointY;
 *    int segmentId;
 *    double segmentDistance;
 *    String sideOfRoad;
 * }
 *
 * Display Manager needs this, I need a way to retrieve it based on a String "point name"
 * need method: Location getLocation(String pointName);
 * Location class {
 *    String pointName aka locationId;
 *    int x;
 *    int y;
 * }
 */
public class DistanceManager {
    /* leftovers from demo version, left here temporarily for reference
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

    private int startDate = 0; // Not implemented
*/
    //int codesScanned = 0; //number of codes scanned so far
    private Stack<String> path = new Stack<String>(); //path so far, saves the String names of the QR codes
    private double totalDistance = 0.0; //keeps track of the total distance of the path

    public static DistanceManager getInstance() {
        return DistanceManager.instance;
    }

    public static DistanceManager instance = new DistanceManager();

    /*
     * Handles QR input (complete, needs official method names, untested)
     * given the name of a location QR code, builds path and tells display manager what to display
     */
    public void processQRCodes(String codeName) {
        //I see a QR code - is either first or not first
        if (path.empty()){            //don't need to pathfind
            //add QRcode to path
            path.push(codeName);

            //grab location from DB (might not have it's own declaration)
            Location firstLocation = DummyDatabaseHelper.getInstance().getLocation(codeName); //DNE //Dave

            //display new point
            DisplayManager.getInstance().drawMarker(firstLocation, false, true); //DNE //Dave
        }
        else {//need to pathfind
            smarterPathFinder(codeName);
        }
        //debug System.out.println("DistanceManager.processQRCodes end");
    }

    /* DEPRICATED
     * Returns sections of path walked First value is the first code scanned,
     * rest values are the segments on the path walked (not in the order they
     * were walked)
     *
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
*/
        /*
         * Returns total distance walked note distance is calculated each time a
         * distanceQR code is scanned
         */
    public double getDistance() {
        return this.totalDistance;
    }

    /* TO EDIT
     * Returns total time on trail in seconds
     */
    public double getTimeOnTrail() {
        long currentTimeMillis = System.currentTimeMillis();
        //TODO set startTimeMillis when drawing first location
        long startTimeMillis=0000000000000;
        long totalTimeMillis = currentTimeMillis - startTimeMillis;
        int totalTimeSeconds = (int)(totalTimeMillis/1000); //convert to seconds

        return totalTimeSeconds;
    }

    /* TO EDIT
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
     */
    //local class for "node"
    class Node implements Comparable<Node> {
        String scanName; //QR code name
        Node parent; //parent node, or null
        double nodeDistance; //total distance from start of subpath
        Segment segment; //segment containing this scanName and previous scan name

        Node (String aScanName, Node aNode, double aDistance, Segment aSegment){
            scanName = aScanName;
            parent = aNode;
            nodeDistance = aDistance;
            segment = aSegment;
        }

        /*NOTE: never returns that two nodes are equal - otherwise the pathfinder will not add
		*   to the path b/c TreeSets allow no duplicates. This is probably temporary unil
		*   the TreeSet collection can be replaced by something without this problem and with
		*   all needed functionality
		*/
        @Override
        public int compareTo(Node left) {
            int returnValue = Double.compare(this.nodeDistance, left.nodeDistance);

            if (returnValue == 0){
                return 1;
            } else {
                return returnValue;
            }
        }
    }//local Node class


        /*A smarter path finder that handles the whole trail
         * handles skipped points, crossing the road ...
         *
         * assumes is not passed the first point seen
         */

    private void smarterPathFinder(String currentScan){
        /** Check if points are adjacent **/
        String lastScan; //most recent point from global path stack
        ArrayList<Segment> attachedSegments; //list of segments, returned from DB //DNE
        Segment currentSegment = null; //current segment we're working with
        boolean pointsAdjacent = false; //if two points are on the same segment

        lastScan = path.peek();//most recent point from path

        attachedSegments = DummyDatabaseHelper.getInstance().getSegmentsWithPoint(currentScan); //DNE //Query DB //segments with currentScan //if nothing found MUST return an empty array list, not null

        //if something goes wrong and attachedSegments is null, skip gracefully (redundant if getSegmentsWithPoint succeeds)
        if (attachedSegments == null){
            System.err.println("Segment list not created. Pathfinding cannot be done. Returning to processQRCode");
            return;
        }

        //if getSegmentsWithPoint grabs nothing b/c error, don't continue because will break
        if (attachedSegments.size() < 1){
            System.err.println("Scanned point "+currentScan+" does not seem to be part of a segment. Pathfinding cannot be done. Returning to processQRCode");
            return;
        }

        //checks if last 2 points scanned are on the same segment
        for (int i = 0; i < attachedSegments.size() && !pointsAdjacent; i++){
            currentSegment = attachedSegments.get(i);
            pointsAdjacent = currentSegment.segmentHasPoints(lastScan, currentScan);
        }

        //we know our next segment, yay!
        if(pointsAdjacent){
            path.push(currentScan);
            totalDistance += currentSegment.getSegmentDistance();//if not initialized, will not be hit
            //display segment
            DisplayManager.getInstance().drawSegment(currentSegment);//DNE

            //display new point
            DisplayManager.getInstance().drawMarker(DummyDatabaseHelper.getInstance().getLocation(currentScan), false, true); //DNE
            return;
        }

        /**if points are not adjacent, pathfind**/

        //(add for optimization?) eliminate segments crossing road if points not on different sides

        //And magic happens - 100% chance this will fail first time it's run...
        //have currentScan(start with this), lastScan(end of the path we've already built)
        /*
        * pathfinder will build unknown path starting with most recent scanned, last point added to
        * pathfinder list will be last point on main path. We then remove last point from list
        * and pop it onto path (adding distance as we go) so that last point left in pathfinder list
        * is most recent point scanned, and now the end of the main path.
        */
        Node currentNode; //first node we add
        TreeSet<Node> subPath; //subpath we're building
        Node shortestNode; //stores the shortest node (list)
        ArrayList<Segment> nextSegments;
        Segment subSegment;
        Node subNode;
        String parentScanName;//name of parent for database query

        currentNode = new Node(currentScan, null, 0.0, null);
        subPath = new TreeSet<Node>();

        subPath.add(currentNode);//add first

        shortestNode = subPath.first(); //get shortest

        while(!shortestNode.scanName.equals(lastScan)) { //while the shortest scan is not the lastScan from path
            if (shortestNode.parent == null){
                parentScanName = null;
            } else {
                parentScanName = shortestNode.parent.scanName;
            }
            nextSegments = DummyDatabaseHelper.getSegmentsWithPoint(shortestNode.scanName, parentScanName);//DNE //Tim


            while (!nextSegments.isEmpty()) {
                subSegment = nextSegments.remove(0); //removes and returns, decreasing list //(is this less optimal than using a for loop?)

                /*
                 * If there's further elimination we want to do (ex. don't add segments that cross the road)
                 *   add them here
                 */

                //DEBUG - if something goes wrong, it's probably here!
                subNode = new Node(subSegment.getOtherPoint(shortestNode.scanName), shortestNode, (subSegment.getSegmentDistance() + shortestNode.nodeDistance), subSegment);//Segment.secondPoint is the point found attached, not the point given to search for
                subPath.add(subNode);
            }

            //all connected points should be in nodes, so must remove original node from subPath
            subPath.pollFirst(); //removes and returns first in set (shortest path here)

            shortestNode = subPath.first(); //get shortest
        }//end while

        //we have our path
        addSubPathToPath(shortestNode);


    }

      /* helper to smarterPathFinder - might fold into smarterPathFinder later
     * given connectingNode.scanName should be lastScan
     * This travels through the list of nodes in connectingNode until end - where end is currentScan
     * - and adds to path
     */
    private void addSubPathToPath(Node shortestNode){
        totalDistance += shortestNode.nodeDistance; //adds subPath distance to total distance
        Node currentNode = shortestNode.parent;
        ArrayList<Segment> segmentsList = new ArrayList<Segment>(); //stores for DisplayManager

        while(currentNode != null){
            path.push(currentNode.scanName);
            currentNode = currentNode.parent;
            segmentsList.add(currentNode.segment);
        }

        //display stuff
        DisplayManager.getInstance().drawSegments(segmentsList);//DNE //Dave

    }
}