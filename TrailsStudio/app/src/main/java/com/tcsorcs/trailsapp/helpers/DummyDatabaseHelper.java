package com.tcsorcs.trailsapp.helpers;

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
}
