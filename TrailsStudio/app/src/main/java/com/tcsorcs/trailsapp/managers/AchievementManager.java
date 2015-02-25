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

	// / AwardAchievement
	// / Awards an achievement
	// / achievementName: Name of the achievement awarded
	public void awardAchievement(String achievementName, String achievementDesc) {
        //Achievement toAward = DatabaseManager.findAchievementByName(achievementName);
		//DisplayManager.getInstance().notifyAchievement(toAward);
	}

    //TODO: put logic in here
    public Achievement FindAchievementByID(int achievementID){
        return new Achievement();
    }

    //TODO: put logic in here
    public ArrayList<Achievement> getAchievementList(){
        return new ArrayList<Achievement>();
    }
}
