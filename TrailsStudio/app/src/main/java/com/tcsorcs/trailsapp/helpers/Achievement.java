package com.tcsorcs.trailsapp.helpers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Achievement
 * Immutable object to hold data returned from the DB.
 * Created by Mike on 2/24/2015.
 */
public class Achievement implements Parcelable {
    public int getAchievementID() {
        return achievementID;
    }

    public String getDateAchieved() {
        return dateAchieved;
    }

    public String getShortDesc() {
        return shortDesc;
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
    private String shortDesc;
    private String dateAchieved;
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
        shortDesc="This achievement is hidden.";
        dateAchieved="NULL";
        icon = "hidden"; //TODO: make hidden achievement icon
        sound = "NULL";
        isAchieved = false;
        isHidden = true;
        isSecret = false;
    }

    /// Basic constructor for an Achievement
    /// Sets the visible properties and stubs the rest.
    public Achievement(int achievementID, String name, String desc, String shortDesc, String dateAchieved, String icon, String sound){
        this.achievementID = achievementID;
        this.name = name;
        this.desc = desc;
        this.shortDesc=shortDesc;
        this.dateAchieved=dateAchieved;
        this.icon = icon;
        this.sound = sound;
        this.isHidden = false;
        this.isSecret = false;
        this.isAchieved = false;
    }

    /// Full constructor for an Achievement
    /// Sets all properties.
    public Achievement(int achievementID, String name, String desc, String shortDesc,String dateAchieved, String icon, String sound, boolean isAchieved, boolean isHidden, boolean isSecret) {
        this.achievementID = achievementID;
        this.name = name;
        this.desc = desc;
        this.shortDesc=shortDesc;
        this.dateAchieved=dateAchieved;
        this.icon = icon;
        this.sound = sound;
        this.isAchieved = isAchieved;
        this.isHidden = isHidden;
        this.isSecret = isSecret;
    }


    // Parcelling part
    public Achievement(Parcel in){
        String[] data = new String[10];

        in.readStringArray(data);

        this.achievementID = Integer.parseInt(data[0]);
        this.name = data[1];
        this.desc = data[2];
        this.shortDesc=data[3];
        this.dateAchieved=data[4];
        this.icon = data[5];
        this.sound = data[6];


        if(data[7].equals("true")){
            this.isAchieved = true;
        }else{
            this.isAchieved = false;
        }

        if(data[8].equals("true")){
            this.isHidden = true;
        }else{
            this.isHidden = false;
        }

        if(data[9].equals("true")){
            this.isSecret = true;
        }else{
            this.isSecret = false;
        }


    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
          Integer.toString(achievementID),
       name,
          desc,
          shortDesc,
          dateAchieved,
          icon,
          sound,
          Boolean.toString(isAchieved),
         Boolean.toString(isHidden),
                Boolean.toString(isSecret)
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Achievement createFromParcel(Parcel in) {
            return new Achievement(in);
        }

        public Achievement[] newArray(int size) {
            return new Achievement[size];
        }
    };





}
