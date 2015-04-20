package com.tcsorcs.trailsapp.helpers;

/**
 * Contains methods and variables related to Segments
 */
public class Segment {

    private int segmentId;
    private String pointA;
    private String pointB;
    private double distance;
    private String segmentName;
    private String sideOfRoad; //east, west or cross
    private boolean onEntrance; //if has a point that is an entrance

    //temp constructor just display manager dev mode for testing
    //TODO remove this constructor after DisplayManager no longer needs for testing drawing segments without database
    public Segment(String segmentName) {
        this.segmentName = segmentName;
    }

    /*
     * Constructs a segment given:
     * segId(int) - the Segment's ID
     * segName(String) - the name of the segment
     * firstPoint(String) - the name of one of the points
     * secondPoint(String) - the name of the other point
     * segmentDistance(double) - the distance of the segment
     * roadSide(String) - the side of road the segment is on - east, west or cross
     * entrance(boolean) - if a segment contains a point that is an entrance
     */
    public Segment (int segId, String segName, String firstPoint, String secondPoint, double segmentDistance, String roadSide, boolean entrance){
        segmentId = segId;
        pointA = firstPoint;
        pointB = secondPoint;
        distance = segmentDistance;
        segmentName = segName;
        sideOfRoad = roadSide;
        onEntrance = entrance;
    }



    //returns segment name
    public String getSegmentName() {
        return segmentName;
    }

    //returns segmentId
    public int getSegmentId(){
        return segmentId;
    }

    //returns first point
    public String getFirstPoint(){
        return pointA;
    }

    //returns second point
    public String getSecondPoint(){
        return pointB;
    }

    //returns distance
    public double getSegmentDistance(){
        return distance;
    }
    
    //returns side of road
    public String getSegmentSideOfRoad(){
    	return sideOfRoad;
    }

    public boolean getSegmentOnEntrance() { return onEntrance; }

    //returns point in segment that is not the given point, or "NullPoint" if given point is not in the segment
    public String getOtherPoint(String somePoint){
        if (somePoint.equals(pointA)){
            return pointB;
        } else if (somePoint.equals(pointB)){
            return pointA;
        } else {
            return "NullPoint";
        }
    }

    //Returns true if this segment has both points, false else
    public boolean segmentHasPoints(String aPoint, String bPoint){
        if (aPoint.equals(pointA) && bPoint.equals(pointB)){
            return true;
        } else if (aPoint.equals(pointB) && bPoint.equals(pointA)){
            return true;
        } else {
            return false;
        }
    }
}
