package com.tcsorcs.trailsapp.helpers;

/**
 * Created by Dave on 2/15/15.
 */
public class SegmentGraphic {

    private String segmentGraphicName;
    private float topLeftX;
    private float topLeftY;

    public String getSegmentGraphicName() {
        return segmentGraphicName;
    }

    public float getTopLeftX() {
        return topLeftX;
    }

    public float getTopLeftY() {
        return topLeftY;
    }

    public int getSegmentId() {
        return segmentId;
    }

    private int segmentId;

    public SegmentGraphic(String segmentGraphicName, float topLeftX, float topLeftY) {
        this.segmentGraphicName = segmentGraphicName;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
    }

}
