package com.tcsorcs.trailsapp.helpers;

/**
 * Created by Dave on 2/15/15.
 */
public class Location {
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private float x;
    private float y;

    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
