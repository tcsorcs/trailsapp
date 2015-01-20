package com.tcsorcs.trailsapp.managers;

public class AchievementManager {

	private static String[] grantedAchievements;
	private static int arrayLength = 20;
	private static int currLoc;

	public AchievementManager() {
		grantedAchievements = new String[arrayLength];
		currLoc = 0;
	}

	public static AchievementManager getInstance() {
		return AchievementManager.instance;
	}

	public static AchievementManager instance = new AchievementManager();

	// / AwardAchievement
	// / Awards an achievement directly (testing only)
	// / achieveName: Name of the achievement awarded
	// / achieveDesc: Description of awarded achievement
	// / Returns a bool of whether the achievement was successfully awarded.
	// / |-> False if achievement has already been granted.
	public boolean awardAchievement(String achieveName,
			String achieveDesc) {
		
		if (achieveName.equals("Executive")) {
			DisplayManager.getInstance().notifyAcheivement(achieveName);
		}
		// // Check if we've pushed this cheevo in yet
		// for(int i = 0; i < currLoc; ++i){
		// if(grantedAchievements[i] == achieveName){
		// // If we have, return false and we're done
		// return false;
		// }
		// }
		// // Otherwise push it in and return true.
		// grantedAchievements[currLoc] = achieveName;
		// ++currLoc;
		return true;
	}
}
