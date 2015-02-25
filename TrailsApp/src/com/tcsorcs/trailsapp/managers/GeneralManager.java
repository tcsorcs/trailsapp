package com.tcsorcs.trailsapp.managers;

import android.app.Activity;

public class GeneralManager {

	public static GeneralManager getInstance() {
		return GeneralManager.instance;
	}

	static GeneralManager instance = new GeneralManager();
	public Activity main_activity = null;
}
