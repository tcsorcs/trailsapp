package com.tcsorcs.trailsapp.managers;

import com.tcsorcs.trailsapp.helpers.Team;

/**
 * Created by Mike on 4/20/2015.
 */
public class SocialManager {

    public static enum JoinReturnCode{
        JOIN_SUCCESSFUL,TEAM_FULL,USER_ON_TEAM,ERROR;
    }

    private Team team;
    public Team getTeam(){
        return this.team;
    }

    public SocialManager() {
        //TODO: check the server for the user's Team, assign them a userID if one has not been set.
        // check server for user's Team data
        // if userID not found, request one to be assigned
        //    +->set in local db
        // if userID assigned to team:
        //    team = new Team(<server data>);
        // else:
        team = new Team();
    }

    public static SocialManager getInstance() {
        return SocialManager.instance;
    }

    public static SocialManager instance = new SocialManager();

    public JoinReturnCode JoinTeam(int teamID){
        //TODO: check server for team membership & availability, then attempt to add user to team
        // result = tryToJoinTeam(teamID);
        // if result = <team is full>
        //   return JoinReturnCode.TEAM_FULL; // Team is full, select another.
        // elif result = <user is already on a team>
        //   return JoinReturnCode.USER_ON_TEAM; // User is on a Team, prompt them to leave first
        // elif result = <join successful>
        UpdateTeamInfo();
        return JoinReturnCode.JOIN_SUCCESSFUL;
        // else
        //   return JoinReturnCode.ERROR; // Other error
    }

    public void UpdateTeamInfo(){
        //TODO: Send userID to the server and build a new Team object from it
        // team = new Team(<server info>);
        team = new Team(1,"The Mighty Milford ORCs",500,100);
    }
}
