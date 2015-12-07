package com.tcsorcs.trailsapp.managers;

import com.tcsorcs.trailsapp.helpers.Goal;

import java.util.ArrayList;

/**
 * Created by Mike on 8/10/2015.
 */
public class FitnessManager {
    private static FitnessManager ourInstance = new FitnessManager();

    private ArrayList<Goal> goalList = new ArrayList();

    public static FitnessManager getInstance() {
        return ourInstance;
    }

    private FitnessManager() {
    }


    public void setGoal(){
        Goal g = new Goal();
        goalList.add(g);
    }

}
