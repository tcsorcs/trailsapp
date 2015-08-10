package com.tcsorcs.trailsapp.helpers;

/**
 * Created by Dave on 2/15/15.
 */
public class Location {

    public String getID() { return id; }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getSideOfRoad() {
        return sideOfRoad;
    }

    private float x;
    private float y;
    private String id;
    private String sideOfRoad;

    public Location(String id, float x, float y, String sideOfRoad) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.sideOfRoad = sideOfRoad;
    }

}
