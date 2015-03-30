package com.tcsorcs.trailsapp.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tcsorcs.trailsapp.helpers.Location;

/**
 * Created by Innovation on 3/3/2015.
 */
public class TrailAppDbHelper extends SQLiteOpenHelper {
    // IF you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TrailApp.db";

    public TrailAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // CRUD (Create, Read, Update, Delete)

    public Location getLocation(String currentScan){

        //TODO query database for location x, y based on location name: currentScan

        return null;
    }

}
