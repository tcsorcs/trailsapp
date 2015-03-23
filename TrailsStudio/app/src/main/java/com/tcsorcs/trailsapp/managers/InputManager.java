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

        //TODO check qrc for valid format, no SQL attacks please!! ^_^


        //TODO integrate achievement manager / distance manager calls upon qrc received here
        //TODO prefix for achievement vs general location QRCode???
        if(qrc.contains("ach:")){
            AchievementManager.getInstance().awardAchievement(qrc);
        }else if(qrc.contains("loc:")){
            DistanceManager.getInstance().processQRCodes(qrc);
        }else if(qrc.contains("triv:")){
            //TriviaManager.getInstance().processQRCodes(qrc);
        }

    }
}
