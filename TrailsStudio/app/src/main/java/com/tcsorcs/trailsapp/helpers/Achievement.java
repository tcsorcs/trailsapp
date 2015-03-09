package com.tcsorcs.trailsapp.helpers;

/**
 * Achievement
 * Immutable object to hold data returned from the DB.
 * Created by Mike on 2/24/2015.
 */
public class Achievement {
    public int getAchievementID() {
        return achievementID;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getIcon() {
        return icon;
    }

    public String getSound() {
        return sound;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isSecret() {
        return isSecret;
    }

    private int achievementID;
    private String name;
    private String desc;
    private String icon;
    private String sound;
    private boolean isAchieved;
    private boolean isHidden;
    private boolean isSecret;

    // Constructor for a Hidden achievement.
    public Achievement(){
        achievementID = -1;
        name = "HIDDEN";
        desc = "This achievement is hidden.";
        icon = "hidden"; //TODO: make hidden achievement icon
        sound = "NULL";
        isAchieved = false;
        isHidden = true;
        isSecret = false;
    }

    /// Basic constructor for an Achievement
    /// Sets the visible properties and stubs the rest.
    public Achievement(int achievementID, String name, String desc, String icon, String sound){
        this.achievementID = achievementID;
        this.name = name;
        this.desc = desc;
        this.icon = icon;
        this.sound = sound;
        this.isHidden = false;
        this.isSecret = false;
        this.isAchieved = false;
    }

    /// Full constructor for an Achievement
    /// Sets all properties.
    public Achievement(int achievementID, String name, String desc, String icon, String sound, boolean isAchieved, boolean isHidden, boolean isSecret) {
        this.achievementID = achievementID;
        this.name = name;
        this.desc = desc;
        this.icon = icon;
        this.sound = sound;
        this.isAchieved = isAchieved;
        this.isHidden = isHidden;
        this.isSecret = isSecret;
    }
}
