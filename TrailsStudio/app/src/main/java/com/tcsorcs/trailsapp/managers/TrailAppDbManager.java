package com.tcsorcs.trailsapp.managers;

import android.content.Context;

import com.tcsorcs.trailsapp.helpers.Location;

import static java.security.AccessController.getContext;

/**
 * Created by Innovation on 3/3/2015.
 */


public class TrailAppDbManager {
    private TrailAppDbHelper mDbHelper = new TrailAppDbHelper(GeneralManager.getInstance().main_activity.getApplicationContext());


    public static TrailAppDbManager getInstance() {
        return TrailAppDbManager.instance;
    }

    public static TrailAppDbManager instance = new TrailAppDbManager();


    public TrailAppDbHelper getDBHelper(){
        return mDbHelper;
    }


}
