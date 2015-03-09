package com.tcsorcs.trailsapp.managers;

import com.tcsorcs.trailsapp.helpers.Achievement;

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
		DisplayManager.getInstance().notifyAcheivement(""+toAward.getAchievementID());
	}
    public void awardAchievement(int achievementID) {
        Achievement toAward = findAchievementByID(achievementID);
        DisplayManager.getInstance().notifyAcheivement(""+toAward.getAchievementID());
    }

    //TODO: put logic in here
    public Achievement findAchievementByID(int achievementID){
        return new Achievement();
    }
    //TODO: put logic in here
    public Achievement findAchievementByName(String achievementName){
        return new Achievement();
    }

    //TODO: put logic in here
    public ArrayList<Achievement> getAchievementList(){
        //For each achievement in the DB
        //  if not secret
        //    if hidden -> add hidden achievement to array
        //    else -> add to array
        return new ArrayList<Achievement>();
    }
}
