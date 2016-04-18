package com.tcsorcs.trailsapp.helpers;

import java.util.ArrayList;

/**
 * TEMPORARY
 * Fakes database - includes methods and variables to be implemented by actual database helper AND
 *   information to fake database returns
 *   Updated 2/17/2016
 *
 *   Singleton
 *
 *  Quick reference for DB tables referenced (not official names):
 *  -PathTable - stores segments, in order of completion, from start of session
 *  -StatsTable - stores stats (distance, time, pace) for current and other sessions
 *  -RecentScannedTable - stores recent QR codes scanned (including time scanned)
 */
public class DummyDatabaseHelper {

    public static DummyDatabaseHelper getInstance() {
        return DummyDatabaseHelper.instance;
    }

    public static DummyDatabaseHelper instance = new DummyDatabaseHelper();

    //for testing purposes only!
    private static int pathTableSize = 0;
    private static ArrayList<Location> thePath = new ArrayList<Location>();

    /** implemented in TrailAppDbHelper
     * Returns all segments with searchPoint as an end point, but without excludeAPoint as an end point
     *  Note that excludeAPoint can be null
     *
     * @param searchPoint each segment in list should contain this point
     * @param excludeAPoint each segment in list should not contain this point, or null, if no points to be excluded
     * @return all segments with searchPoint as an end point, but without excludeAPoint as an end point.
     *         If no segments exist, return null.
     *
     * NOTE: if DB helper is not referenced statically, remove static declaration. Keep else.
     */
    public ArrayList<Segment> getSegmentsWithPoint(String searchPoint, String excludeAPoint) {


        //TODO query database for list of segements that each segment includes searchPoint, but does not include excludeAPoint

        return null;
    }

    /** Not used in DistanceManager - if not used anywhere, please delete
     * Retrieve segments that contain the current scan location
     *
     * @param currentScan string representing current scan location example: "l21", "execent","l19" etc...
     * @return  ArrayList of segments that contain currentScan as one of their endpoints
     */
    public ArrayList<Segment> getSegmentsWithPoint(String currentScan){

        Location currentLoc=getLocation(currentScan);
        float x= currentLoc.getX();
        float y=currentLoc.getY();

        //TODO query database for all segments that have x,y as one of their points

        return null;
    }

    /** TrailAppDbHelper
     *
     * @param currentScan string representing current scan location example: "l21", "execent","l19" etc...
     * @return Location object with x,y coordinates for this scan
     */
    public Location getLocation(String currentScan){

        //TODO query database for location x, y based on location name: currentScan

        return null;
    }

    /**
     * Checks a segment and returns what side of the road it's on (east, west, cross)
     * @param currentScan segment to check side of road from
     * @return String with "east", "west" or "cross"
     */
    public String getSideOfRoad(String currentScan){
        /* will test later
        String sideOfRoad;
        sideOfRoad = getSideOfRoad(getLocation(currentScan));

        return sideOfRoad;
        */
        return "left";//temporary to use for simple button testing
    }

    /**
     * Checks a Location and returns what side of the road it's on (east, west)
     * @param currentSegment Location to check side of road from
     * @return String with "east", "west" or "cross"
     */
    public String getSideOfRoad(Segment currentSegment){
        //TODO query database for Segment's side of road

        return null;
    }

    /**
     * Checks a Location and returns what side of the road it's on (east, west)
     * @param currentLocation Location to check side of road from
     * @return String with "east" or "west"
     */
    public String getSideOfRoad(Location currentLocation){
        //TODO query database for Location's side of road

        return null;
    }



    /**
     * Retreive list of all achievements in the database
     *
     * @return ArrayList of all achievements in the database
     */
    public ArrayList<Achievement> getAchievementList(){

        //TODO query database for full list of achievements

        return null;
    }

    /**
     *Retreive list of recent achievements that have been accomplished.
     *
     * @return ArrayList of recent achievements in the database
     */
    public ArrayList<Achievement> getRecentAchievementList(){

        //TODO query database for achievements having isAchieved true and having date within ? 30 days?

        return null;
    }


    /**
     * Retreive an Achievement based on achievement's achievement id
     *
     * @param achievementId Integer id of ahcievement to be retrieved
     * @return Achievement object assocated with the achievementId
     */
    public Achievement getAchievementById(int achievementId){

        //TODO query database for Achievement object assocated with achievementId

        return null;
    }

    /**
     * Retrieve achievement object based on an achievement's name
     *
     * @param achievementName String name of achievement to be retreived
     * @return Achievement object assocated with the achievementName
     */
    public Achievement getAchievementByName(String achievementName){

        //TODO query database for Achievement object assocated with achievementName


        return null;
    }



    /**
     * Returns the SegmentGraphic object associated with the passed segmentId
     *
     * @param segmentId Integer id of the segment to look up GraphicSegment for
     * @return SegmentGraphic object associated with the segmentId
     */
    public SegmentGraphic getSegmentGraphic(int segmentId){

        //TODO query database for the SegmentGraphic assocated with segmentId

        return null;
    }

    /**
     * Adds new Location to PathTable (table storing path Locations and order added)
     *
     * @param newLocation a Segment to add to PathTable
     */
    public void addLocationToPath(Location newLocation){
        //TODO add Location to PathTable
        thePath.add(newLocation);
        pathTableSize+=1;
    }

    /**
     * Returns the number of entries in the PathTable
     *
     * @return size an Integer with the number of entries in the table
     */
    public Integer getPathTableSize(){
        //TODO returns number of entries in the PathTable
        return pathTableSize;
    }

    public Location getLastLocation(){
        //TODO returns last location stored in PathTable
        return thePath.get(pathTableSize - 1);
    }

    /**
     * Replaces current distance with a new distance to StatsTable (table storing current distance, pace, etc.)
     *
     * @param newDistance the new current distance to store
     */
    public void addDistance(double newDistance){
        //TODO replaces current distance with new distance
    }

    /**
     * Returns current distance from StatsTable (table storing current distance, pace, etc.)
     *
     * @return currentDistance the current distance
     */
    public Double getDistance(){
        //TODO returns the current distance
        return 0.0;
    }

    /**
     * Replaces current pace with a new pace to StatsTable (table storing current distance, pace, etc.)
     *
     * @param newPace the new current pace to store
     */
    public void addPace(double newPace){
        //TODO replaces current pace with new pace
    }

    /**
     * Returns current pace from StatsTable (table storing current distance, pace, etc.)
     *
     * @return currentPace the current pace
     */
    public Double getPace(){
        //TODO returns the current pace
        return 0.0;
    }

    /**
     * Returns start time from StatsTable (table storing current distance, pace, etc.)
     *
     * @return Start Time
     */
    public Long getStartTime(){
        //TODO returns the start time
        return 0L;
    }
}
