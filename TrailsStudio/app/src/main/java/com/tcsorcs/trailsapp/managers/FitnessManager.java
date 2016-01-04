package com.tcsorcs.trailsapp.managers;

import com.tcsorcs.trailsapp.helpers.Goal;

import java.util.ArrayList;

/**
 * Created by Mike on 8/10/2015.
 */
public class FitnessManager {
    private static FitnessManager ourInstance = new FitnessManager();


    private ArrayList<Goal> goalList = new ArrayList();
    private Goal currentGoal;

    public static FitnessManager getInstance() {
        return ourInstance;
    }

    private FitnessManager() {
    }


    /** CREATE NEW GOAL
     * Creates a new goal to begin editing
     * New goal can be modified with mutators below
     */
    public void createNewGoal(){
        Goal currentGoal = new Goal();

    }

}
