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

	public void awardAchievement(String achievementName) {
        Achievement toAward = findAchievementByName(achievementName);
		DisplayManager.getInstance().displayAchievement(toAward);
	}
    public void awardAchievement(int achievementID) {
        Achievement toAward = findAchievementByID(achievementID);
        DisplayManager.getInstance().displayAchievement(toAward);
    }

    public Achievement findAchievementByID(int achievementID){
        //TODO: Replace with actual DAO calls once implemented
        Achievement ach =DummyDatabaseHelper.getInstance().getAchievementById(achievementID);

        return ach;
    }
    public Achievement findAchievementByName(String achievementName){
        //TODO: Replace with actual DAO calls once implemented
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
