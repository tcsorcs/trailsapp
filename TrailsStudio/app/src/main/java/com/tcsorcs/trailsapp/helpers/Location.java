package com.tcsorcs.trailsapp.helpers;

/**
 * Created by Dave on 2/15/15.
 */
public class Location {

    public String getID() { return id;}

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private float x;
    private float y;
    private String id;

    public Location(String id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Location(String id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.sideOfRoad = null;
    }

}
