package com.tcsorcs.trailsapp;

class AchievementManager {
  public static AchievementManager getInstance() {
	  return AchievementManager.instance;
  }
  static AchievementManager instance = new AchievementManager();
}
