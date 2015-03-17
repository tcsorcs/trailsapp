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

    //stuff will be added here...
    /*
    *  Returns all segments with searchPoint as an end point, but without excludeAPoint as an end point
    *  Note that excludeAPoint can be null
     */
    public static ArrayList<Segment> getSegmentsWithPoint(String searchPoint, String excludeAPoint) {
        return null;
    }
}
