package com.tcsorcs.trailsapp.managers;

import android.content.Context;

import static java.security.AccessController.getContext;

/**
 * Created by Innovation on 3/3/2015.
 */
public class TrailAppDbManager {
    TrailAppDbHelper mDbHelper = new TrailAppDbHelper(GeneralManager.getInstance().main_activity.getApplicationContext());
}
