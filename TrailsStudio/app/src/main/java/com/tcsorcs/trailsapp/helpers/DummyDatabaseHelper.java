package com.tcsorcs.trailsapp.helpers;

import java.util.ArrayList;

/**
 * TEMPORARY
 * Fakes database - includes methods and variables to be implemented by actual database helper AND
 *   information to fake database returns
 *
 *   Singleton
 */
public class DummyDatabaseHelper {

    public static DummyDatabaseHelper getInstance() {
        return DummyDatabaseHelper.instance;
    }

    public static DummyDatabaseHelper instance = new DummyDatabaseHelper();


    /**
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
    public static ArrayList<Segment> getSegmentsWithPoint(String searchPoint, String excludeAPoint) {


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

    /**
     *
     * @param currentScan string representing current scan location example: "l21", "execent","l19" etc...
     * @return Location object with x,y coordinates for this scan
     */
    public static Location getLocation(String currentScan){

        //TODO query database for location x, y based on location name: currentScan

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
    public static void addLocationToPath(Location newLocation){
        //TODO add Location to PathTable
    }

    /**
     * Returns the number of entries in the PathTable
     *
     * @return size an Integer with the number of entries in the table
     */
    public static Integer getPathTableSize(){
        //TODO returns number of entries in the PathTable
        return 0;
    }


    public static Location peekLastLocation(){
        //TODO returns last location stored in PathTable

        return null;
    }
}
