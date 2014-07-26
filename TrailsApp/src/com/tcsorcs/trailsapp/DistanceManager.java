package com.tcsorcs.trailsapp;

class DistanceManager {
  public static DistanceManager getInstance() {
	  return DistanceManager.instance;
  }
  static DistanceManager instance = new DistanceManager();
}
