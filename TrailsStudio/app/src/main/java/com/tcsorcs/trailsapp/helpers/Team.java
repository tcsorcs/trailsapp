package com.tcsorcs.trailsapp.helpers;

/**
 * Name: Team
 * Details: Immutable object to hold Team data received from the server.
 * Creator: Mike
 * Create Date: 4/20/2015
 */
public class Team {

    private int teamID;
    private String teamName;
    private float teamDistance;
    private int teamAchievements;

    public int getTeamID() {
        return teamID;
    }
    public String getTeamName() {
        return teamName;
    }
    public float getTeamDistance() {
        return teamDistance;
    }
    public int getTeamAchievements() {
        return teamAchievements;
    }

    // Default Constructor
    // This should only be used if the user has not yet been assigned to a team.
    public Team(){
        teamID = -1;
        teamName = "DEFAULT";
        teamDistance = -1f;
        teamAchievements = -1;
    }

    // Constructor
    // Used to build the Team object from the data received by the app from the server.
    public Team(int teamID, String teamName, float teamDistance, int teamAchievements) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.teamDistance = teamDistance;
        this.teamAchievements = teamAchievements;
    }

    @Override
    public String toString(){
        String outStr = teamName+" (#"+teamID+") have covered a combined distance of "+teamDistance;
        outStr+=", and together they have unlocked "+teamAchievements+" achievements!";
        return outStr;
    }
}
