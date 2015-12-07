package com.tcsorcs.trailsapp.managers;

import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Arrays;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.helpers.DummyDatabaseHelper;
import com.tcsorcs.trailsapp.helpers.Location;

import android.net.Uri;
import android.os.DropBoxManager;
import android.widget.Toast;

public class InputManager {


    private final String ACHIEVEMENT="ach"; //parameter for achievement
    private final String LOCATION="loc"; //parameter for location (qr code scan, not sms share)
    private final String SMS="sms"; //parameter for sms sharing locations
    private final String TRIVIA="triv"; //parameter for trivia


    //list of valid qrcode types to check against when validating a scan or sms open
    private final String[] VALID_QRC_TYPES = new String[] { ACHIEVEMENT,
            LOCATION, SMS, TRIVIA};

    public static InputManager getInstance() {
        return InputManager.instance;
    }
    public static InputManager instance = new InputManager();

    private boolean validQRCode=true; // keeps track during validation if qrcode is valid format

    public void inputQRC(Uri qrc) {

        //retreive type parameter to get qr code type (ach, loc, sms, triv)
        String type=qrc.getQueryParameter("type");
        if(Arrays.asList(VALID_QRC_TYPES).contains(type)){

            if(type.equals(ACHIEVEMENT)){
                //data holds the achievement name , a1, a2, kingofthenorth or whatever the achievement is called
                String achievementName=qrc.getQueryParameter("data");
                if(achievementName!=null && achievementName.length()>0){

                    Toast.makeText(DisplayManager.getInstance().main_activity.getApplicationContext(), "Achievement: "+achievementName, Toast.LENGTH_LONG).show();

                    //TODO achievement manager will check if achievemnt has a location associated with it, if it does then call DisplayManager.getInstance().drawAchievementMarker(loc,true,true)
                    AchievementManager.getInstance().awardAchievement(achievementName);

                }else{
                    //invalid achievement name
                    validQRCode=false;
                }
            }else if(type.equals(LOCATION)){
                //data parameter holds location name such as l21, l20, exeent, depeent
                String locationName=qrc.getQueryParameter("data");
                if(locationName!=null && locationName.length()>0){

                    Toast.makeText(DisplayManager.getInstance().main_activity.getApplicationContext(), "Location: "+locationName, Toast.LENGTH_LONG).show();

                   //retreive location object from DB
                    Location locDB= TrailAppDbManager.getInstance().getDBHelper().getLocation(locationName);

                    if(locDB!=null){
                        //lcoation found in db, so draw location on map from qrc
                        DisplayManager.getInstance().drawMarker(locDB,true,true);
                    }else{
                        //unable to find location in DB
                        Toast.makeText(DisplayManager.getInstance().main_activity.getApplicationContext(), "Unable to find Location in DB: "+locationName, Toast.LENGTH_LONG).show();
                    }

                    //TODO hook up distance manager
                    //passing l21, l20, exceent, or whatever the scan data string is on to distance manager
                    //DistanceManager.getInstance().processQRCodes(locationName);

                }else{
                    //invalid location name in data parameter
                    validQRCode=false;
                }

            }else if(type.equals(SMS)){


                //locations on bitmap to draw marker
                //sms type has an x param and a y param
                String xStr=qrc.getQueryParameter("x");
                String yStr=qrc.getQueryParameter("y");

                if (xStr != null && yStr != null) {
                    int xInt=0;
                    int yInt=0;
                    try{

                        xInt = Integer.parseInt(xStr);
                        yInt = Integer.parseInt(yStr);

                        //Toast.makeText(DisplayManager.getInstance().main_activity.getApplicationContext(), "SMS: x="+xStr+" y="+yStr, Toast.LENGTH_LONG).show();

                        //draw sms shared location to map
                        Location loc=new Location("0",xInt,yInt,"");
                        DisplayManager.getInstance().drawMarker(loc,true,true);

                    }catch(NumberFormatException e){
                        //unable to parse x or y to an int
                        validQRCode=false;
                    }
                }else{
                    //invalid x or y
                    validQRCode=false;

                }
            }else if(type.equals(TRIVIA)){

                String triviaName=qrc.getQueryParameter("data");
                if(triviaName!=null && triviaName.length()>0){

                    Toast.makeText(DisplayManager.getInstance().main_activity.getApplicationContext(), "Trivia: "+triviaName, Toast.LENGTH_LONG).show();

                    //TODO handle triva QR CODE


                }else{
                    //invalid triva name in data field
                    validQRCode=false;
                }



            }
        }else{
            //not a valid QR code type
            validQRCode=false;
        }

        //check for validation errors, and present error to user if necessary
          if(!validQRCode){
                Toast.makeText(DisplayManager.getInstance().main_activity.getApplicationContext(), DisplayManager.getInstance().main_activity.getString(R.string.invalid_qrc) , Toast.LENGTH_LONG).show();
              //reset flag for next scan
              validQRCode=true;
         }


    }
}
