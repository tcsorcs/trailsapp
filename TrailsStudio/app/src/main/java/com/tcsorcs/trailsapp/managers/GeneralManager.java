package com.tcsorcs.trailsapp.managers;

import android.support.v7.app.ActionBarActivity;

public class GeneralManager {

    public static GeneralManager getInstance() {
        return GeneralManager.instance;
    }

    static GeneralManager instance = new GeneralManager();
    public ActionBarActivity main_activity = null;
}
