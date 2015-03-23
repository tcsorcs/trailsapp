package com.tcsorcs.trailsapp.managers;

import com.tcsorcs.trailsapp.helpers.Achievement;
import com.tcsorcs.trailsapp.helpers.DummyDatabaseHelper;

import java.util.ArrayList;
import java.util.Hashtable;

public class AchievementManager {

	public AchievementManager() {
	}

	public static AchievementManager getInstance() {
		return AchievementManager.instance;
	}

	public static AchievementManager instance = new AchievementManager();

    //TODO: change to pass an int once display man handles it
	public void awardAchievement(String achievementName) {
        Achievement toAward = findAchievementByName(achievementName);
		DisplayManager.getInstance().displayAchievement(toAward);
	}
    public void awardAchievement(int achievementID) {
        Achievement toAward = findAchievementByID(achievementID);
        DisplayManager.getInstance().displayAchievement(toAward);
    }

    //TODO: put logic in here
    public Achievement findAchievementByID(int achievementID){

        Achievement ach =DummyDatabaseHelper.getInstance().getAchievementById(achievementID);

        return ach;
    }
    //TODO: put logic in here
    public Achievement findAchievementByName(String achievementName){

        Achievement ach =DummyDatabaseHelper.getInstance().getAchievementByName(achievementName);

        return ach;
    }

    //TODO: put logic in here
    public ArrayList<Achievement> getAchievementList(){

       ArrayList<Achievement> achievements= DummyDatabaseHelper.getInstance().getAchievementList();


        //For each achievement in the DB
        //  if not secret
        //    if hidden -> add hidden achievement to array
        //    else -> add to array
        return new ArrayList<Achievement>();
    }

    //TODO: put logic in here
    public ArrayList<Achievement> getRecentAchievementList(){

        ArrayList<Achievement> recentAchievements=  DummyDatabaseHelper.getInstance().getRecentAchievementList();

        return recentAchievements;
    }

}
