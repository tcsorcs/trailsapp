package com.tcsorcs.trailsapp.managers;

import java.util.Arrays;

import android.widget.Toast;

public class InputManager {
	public static final String[] VALID_SCANPOINTS = new String[] { "ExceEnt",
			"L21", "L20", "L18", "DepeEnt" };

	public static InputManager getInstance() {
		return InputManager.instance;
	}

	static InputManager instance = new InputManager();

	public void inputQRC(String qrc) {

		if (qrc.equals("Executive")) {
			// direct achievement scanned
			AchievementManager.getInstance().awardAchievement(qrc, "N/A");
		} else if (Arrays.asList(VALID_SCANPOINTS).contains(qrc)) {
			DistanceManager.getInstance().processQRCodes(qrc);
		}
	}
}
