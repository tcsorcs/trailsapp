package com.tcsorcs.trailsapp.managers;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.activities.AchievementDialogActivity;
import com.tcsorcs.trailsapp.activities.GoogleMapsActivity;
import com.tcsorcs.trailsapp.activities.MainTrailsActivity;
import com.tcsorcs.trailsapp.helpers.Achievement;
import com.tcsorcs.trailsapp.helpers.DummyDatabaseHelper;
import com.tcsorcs.trailsapp.helpers.Location;
import com.tcsorcs.trailsapp.helpers.Segment;
import com.tcsorcs.trailsapp.helpers.SegmentGraphic;
import com.tcsorcs.trailsapp.mapview.TouchImageView;
import com.tcsorcs.trailsapp.mapview.TouchImageView.DoubleTapZoom;
import com.tcsorcs.trailsapp.services.OnTrailsService;

/*
 *
 * Pathfinder testing in progress - I'll be hijacking all the M buttons but returning them to a stable state before comitting, so please DON'T DELETE COMMENTED CODE! I still need it.  - Sarah, Mar 6
 */
public class DisplayManager {

    public static DisplayManager getInstance() {
        return DisplayManager.instance;
    }

    public static DisplayManager instance = new DisplayManager();
    public ActionBarActivity main_activity = null;

    // unit labels for textview Pace, Distance Time on Trails
    private final String DISTANCE_UNITS = "feet";
    private final String PACE_UNITS = "ft/min";

    private boolean onTrailsServiceRunning = false; // boolean to keep track if service running

    // used in updating textviews while a person is on the trails, real time stats
    private TextView timeOnTrail;
    private TextView totalDistance;
    private TextView currentPace;

    // runnables for updating pace, time on trails, distance at specific
    // intervals on separate threads
    private Runnable timeOnTrailsViewRunnable = null;
    private Runnable paceViewRunnable = null;
    private Runnable distanceViewRunnable = null;

    // the map view on the home activity
    private TouchImageView mapPanView;

    //canvas for painting onto the map view
    private Canvas canvas=null;

    // color for fill of base marker
    private final int markerOuterColor=Color.RED;
    //color for fill of inner circle on marker
    private final int markerInnerColor=Color.YELLOW;

    private final int achievementMarkerOuterColor=Color.GREEN;
    //private int achievementMarkerInnerColor=Color.YELLOW;


    //size of radius for markers when drawing achievement markers or general markers
    private final int markerOuterRadius=16;
    private final int markerInnerRadius=8;

    //max zoom level of trails map
    private final int markerZoomLevel=6;

    private Location previousLoc=null; //marker to color over on a new scan

    private int previousOuterColor=0; //remember previous marker color for recoloring

    //dev mode graphic segments for testing
    private SegmentGraphic segGraphicS1=new SegmentGraphic("execent_l21",1528.839f,1843.433f);
    private SegmentGraphic segGraphicS2=new SegmentGraphic("l20_l21",1469.213f,1820.99f);
    private SegmentGraphic segGraphicS3=new SegmentGraphic("l19_l20",1428.006f,1778.52f);
    private SegmentGraphic segGraphicS4=new SegmentGraphic("l18_l19",1433.737f,1604.69f);
    private SegmentGraphic segGraphicS5=new SegmentGraphic("depeent_l18",1694.443f,1603.73f);

    private ArrayList<Achievement> achievementList=new ArrayList<Achievement>(); //dev mode achievement list
    private String longPressLink=null; //store link copied to clipboard for sms sharing

    //main trails map height and width
    private final int mapWidth=4000;
    private final int mapHeight=2818;

    private Boolean inDevMode=false; //true if in dev mode

    private ProgressDialog progressDialog = null; //used for loading screens


    private Bitmap drawnBitmap = null; //used in canvas for main trails image



    /**
     * Sets click listeners for all buttons on MainTrailsActivity. Does not set click listeners
     * for fragment buttons. Fragment buttons handle their own on click listeners
     */
    public void setButtonCallbacks() {


        //my location button
        ImageButton shortestPathBtn = (ImageButton) main_activity
                .findViewById(R.id.ShortestPath);
        shortestPathBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                //TODO develop shortest path

                Toast.makeText(main_activity,"To be developed.",Toast.LENGTH_SHORT).show();


            }
        });

        //my location button
        ImageButton myLocation = (ImageButton) main_activity
                .findViewById(R.id.MyLocation);
        myLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                showLoadingDialog();

                //open google maps activity
                Intent i = new Intent(main_activity,GoogleMapsActivity.class);
                main_activity.startActivity(i);


            }
        });


        //Scan QR Code Button
        ImageButton scanBarcodeButton = (ImageButton) main_activity
                .findViewById(R.id.ScanBarcodeButton);
        scanBarcodeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                //package name for QR Code Reader App
                final String appPackageName = "me.scan.android.client";

                //check if qRCodeReader is installed
                if (isPackageInstalled(appPackageName, main_activity)) {
                    //if it is, go to it so we can scan a qr code
                    //format of url is given by QR Code Reader Documentaiton
                    //https://scan.me/
                    //https://scan.me/help#/article/1363347
                    String url = "scan://scan?callback=com.tcsorcs.trailsapp://tcstrails";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    main_activity.startActivity(i); //Getting error "android.content.ActivityNotFoundException: No Activity found to handle Intent" when this is called

                } else {
                    //if app is not installed, go to installation page in market
                    try {
                        main_activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        //play store isn't installed on device, go to app in a browser
                        main_activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }
        });

        /**
         * EVERYTHING BELOW IN THIS BUTTON CALLBACKS METHOD IS FOR DEVELOPMENT TESTING
         */



        // DEV MODE MARKER BUTTONS

        Button markerButton = (Button) main_activity
                .findViewById(R.id.M1); //ExceEnt
        markerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                disableButtons();

                /* for pathfinder test - in progress - WARNING! ExceEnt is not in the database (checked Mar 6)
                * commented code works - passes correctly through InputManager and hits Distance manager
                //fake-o url but data gets to Distance Manager correctly

                String url = "com.tcsorcs.trailsapp://halp?type=loc&data=ExceEnt";
                InputManager.getInstance().inputQRC(Uri.parse(url));
                */

                float x = 1694f;
                float y = 1850f;
                Location loc=new Location("0",x,y,"");
                drawMarker(loc, true, true);
            }
        });

        final Button achievementOne = (Button) main_activity
                .findViewById(R.id.A1);
        achievementOne.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {



                SoundManager.getInstance().playHooray("hooray");




                int id=-1;
                String name="Executive Achievement";
                String longDesc="This is a long description of the Executive Achievement.";
                String shortDesc="Walk the executive loop trail.";
                String icon="ic_launcher";
                String sound="";
                boolean isAchieved=false;
                boolean isHidden=false;
                boolean isSecret=false;

                Achievement a= new Achievement(id,name,longDesc,shortDesc,new Date().toString(),icon,sound,isAchieved,isHidden,isSecret);

                achievementList.add(0,a);
                displayAchievement(a);
            }
        });

        Button achievementTwo = (Button) main_activity
                .findViewById(R.id.A2);
        achievementTwo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                float  x = 2181f;
                float  y = 118f;
                Location loc=new Location("0",x,y,"");

                drawAchievementMarker(loc,true,true);


                SoundManager.getInstance().playKing("kinginthenorth");

                int id=-1;
                String name="King of the North";
                String longDesc="This is a long description of the King of the North Achievement.";
                String shortDesc="Reach the northern most point of the trails.";
                String icon="kinginthenorth";
                String sound="";
                boolean isAchieved=false;
                boolean isHidden=false;
                boolean isSecret=false;

                Achievement a= new Achievement(id,name,longDesc,shortDesc,new Date().toString(),icon,sound,isAchieved,isHidden,isSecret);


                achievementList.add(0,a);

                displayAchievement(a);
            }
        });

        Button achievementThree = (Button) main_activity
                .findViewById(R.id.A3);
        achievementThree.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {



                float x = 1694f;
                float y = 1850f;
                Location loc=new Location("0",x,y);
                drawAchievementMarker(loc,false,true);

                x = 1534f;
                y = 1963f;
                Location loc2=new Location("0",x,y);
                drawAchievementMarker(loc2,false,true);


                x = 1473f;
                y = 1826f;
                Location loc3=new Location("0",x,y);
                drawAchievementMarker(loc3,true,true);

                SoundManager.getInstance().playKirby("kirby");


                int id=-1;
                String name="Super Scan Bros";
                String longDesc="This is a long description of the Super Scan Bros Achievement.";
                String shortDesc="Scan 3 trail markers within 3 minutes.";
                String icon="kirby";
                String sound="";
                boolean isAchieved=false;
                boolean isHidden=false;
                boolean isSecret=false;

                Achievement a= new Achievement(id,name,longDesc,shortDesc,new Date().toString(),icon,sound,isAchieved,isHidden,isSecret);

                achievementList.add(0,a);
                displayAchievement(a);
            }
        });

        markerButton = (Button) main_activity
                .findViewById(R.id.M2); //L21
        markerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                disableButtons();
                // Toast.makeText(main_activity.getApplicationContext(), "click", Toast.LENGTH_LONG).show();

                /* for pathfinder test - in progress
                * commented code works - passes correctly through InputManager and hits Distance manager
                //fake-o url but data gets to Distance Manager correctly
                */
                String url = "com.tcsorcs.trailsapp://halp?type=loc&data=L21";
                InputManager.getInstance().inputQRC(Uri.parse(url));

                float x = 1534f;
                float y = 1963f;
                Location loc=new Location("0",x,y);
                drawMarker(loc,  true, true);
            }
        });



        markerButton = (Button) main_activity
                .findViewById(R.id.M3); //L20
        markerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                disableButtons();

                // Toast.makeText(main_activity.getApplicationContext(), "click", Toast.LENGTH_LONG).show();

                /* for pathfinder test - in progress
                * commented code works - passes correctly through InputManager and hits Distance manager
                //fake-o url but data gets to Distance Manager correctly
                */
                String url = "com.tcsorcs.trailsapp://halp?type=loc&data=L20";
                InputManager.getInstance().inputQRC(Uri.parse(url));

                float x = 1473f;
                float y = 1826f;
                Location loc=new Location("0",x,y);
                drawMarker(loc,  true, true);
            }
        });

        markerButton = (Button) main_activity
                .findViewById(R.id.M4); //L19
        markerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                disableButtons();

                // Toast.makeText(main_activity.getApplicationContext(), "click", Toast.LENGTH_LONG).show();

                /* for pathfinder test - in progress
                * commented code works - passes correctly through InputManager and hits Distance manager
                //fake-o url but data gets to Distance Manager correctly
                */
                String url = "com.tcsorcs.trailsapp://halp?type=loc&data=L19";
                InputManager.getInstance().inputQRC(Uri.parse(url));

                float x = 1434f;
                float y = 1784f;
                Location loc=new Location("0",x,y);
                drawMarker(loc,  true, true);
            }
        });

        markerButton = (Button) main_activity
                .findViewById(R.id.M5); //L18
        markerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                disableButtons();
                // Toast.makeText(main_activity.getApplicationContext(), "click", Toast.LENGTH_LONG).show();

                /* for pathfinder test - in progress
                * commented code works - passes correctly through InputManager and hits Distance manager
                //fake-o url but data gets to Distance Manager correctly
                */
                String url = "com.tcsorcs.trailsapp://halp?type=loc&data=L18";
                InputManager.getInstance().inputQRC(Uri.parse(url));

                float x = 1703f;
                float y = 1602f;
                Location loc=new Location("0",x,y);
                drawMarker(loc,  true, true);
            }
        });

        markerButton = (Button) main_activity
                .findViewById(R.id.M6); //DepeEnt
        markerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                disableButtons();
                // Toast.makeText(main_activity.getApplicationContext(), "click", Toast.LENGTH_LONG).show();

                /* for pathfinder test - in progress
                * commented code works - passes correctly through InputManager and hits Distance manager
                //fake-o url but data gets to Distance Manager correctly
                */
                String url = "com.tcsorcs.trailsapp://halp?type=loc&data=DepeEnt";
                InputManager.getInstance().inputQRC(Uri.parse(url));

                float x = 1724f;
                float y = 1651f;
                Location loc=new Location("0",x,y);
                drawMarker(loc,  true, true);
            }
        });


        Button clearButton = (Button) main_activity
                .findViewById(R.id.ClearButton);
        clearButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                clearTrailsMap();

            }
        });

        Button segmentButton = (Button) main_activity
                .findViewById(R.id.S1);
        segmentButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Segment seg=new Segment("execent_l21");
                drawSegment(seg);


            }
        });


        segmentButton = (Button) main_activity
                .findViewById(R.id.S2);
        segmentButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Segment seg=new Segment("l20_l21");
                drawSegment(seg);


            }
        });

        segmentButton = (Button) main_activity
                .findViewById(R.id.S3);
        segmentButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Segment seg=new Segment("l19_l20");
                drawSegment(seg);


            }
        });

        segmentButton = (Button) main_activity
                .findViewById(R.id.S4);
        segmentButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Segment seg=new Segment("l18_l19");
                drawSegment(seg);


            }
        });

        segmentButton = (Button) main_activity
                .findViewById(R.id.S5);
        segmentButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Segment seg=new Segment("depeent_l18");
                drawSegment(seg);


            }
        });

        // END DEV MODE BUTTONS
    }

    /**
     *
     * @return return list of achievements used in dev mode
     */
    public ArrayList<Achievement> getAchievementList(){
        return achievementList;
    }



    /**
     * if currently gathering pace, time, distance from Display manager- remove
     * runnables that are updating these to save system resources when our
     * activity is not in view
     */
    public void stopGatheringTime() {

        //cancel thread collecting time
        if (timeOnTrailsViewRunnable != null) {
            timeOnTrail.removeCallbacks(timeOnTrailsViewRunnable);
            timeOnTrailsViewRunnable = null;

        }

    }

    /**
     * if onTrailsService is running, start updating textviews for pace, time,
     * distance
     */
    public void beginGatheringTime() {

//        if (onTrailsServiceRunning) {
//            timeOnTrail = (TextView) main_activity
//                    .findViewById(R.id.time_on_trail_text);
//            totalDistance = (TextView) main_activity
//                    .findViewById(R.id.distance_text);
//            currentPace = (TextView) main_activity.findViewById(R.id.pace_text);
//
//            // TIME ON TRAILS RUNNABLE
//
//            //start thread to begin gathering time
//
//            timeOnTrailsViewRunnable = new Runnable() {
//                @Override
//                public void run() {
//
//
//                    String convertedTime = convertSecondsToTimeString(DistanceManager.getInstance()
//                            .getTimeOnTrail());
//
//                    timeOnTrail.setText(convertedTime);
//
//                    timeOnTrail.postDelayed(timeOnTrailsViewRunnable, 1000);
//                }
//            };
//            // add to message queue
//            timeOnTrail.post(timeOnTrailsViewRunnable);
// }

    }

    /**
     * stops the notification and service since user is no longer on a trail
     */
    public void stopOnTrailsService() {

        onTrailsServiceRunning = false;
        // stop service if it is running / if app is killed from task manager
        // this makes sure that our service is killed also upon killing our app
        main_activity.stopService(new Intent(main_activity,
                OnTrailsService.class));


        DistanceManager.instance = new DistanceManager();

    }

    /**
     * sets the initial map view for the home activity before any trail qr is
     * scanned
     */
    public void initializeMapView() {
        mapPanView = (TouchImageView) main_activity
                .findViewById(R.id.MapPanView);
        // set max zoom a user can zoom into the map when double tapping or
        // pinching
        mapPanView.setMaxZoom(10);

        //trails map that will be drawn on the canvas

         drawnBitmap = null;

        try {

            drawnBitmap = Bitmap.createBitmap(mapWidth , mapHeight, Bitmap.Config.RGB_565);

            canvas = new Canvas(drawnBitmap);

            //top left corner to begin drawing trails map, bottom right is height and width
            int mapX=0;
            int mapY=0;


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap1 = BitmapFactory.decodeResource(main_activity.getResources(), R.drawable.trails_nomarkup, options);
            canvas.drawBitmap(bitmap1, null, new Rect(mapX,mapY,mapWidth,mapHeight), null);
            bitmap1.recycle();
            mapPanView.invalidate();


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //longpress will copy coordinates of the bitmap for sharing
        mapPanView.setCopyLocationOnLongPress(true);

        //set bitmap drawing to the TouchImageView
        mapPanView.setImageBitmap(drawnBitmap);

        mapPanView.invalidate();

       mapPanView.setZoom(2.2f);

        mapPanView.setScaleType(ImageView.ScaleType.CENTER);

        mapPanView.setMinZoom(2.2f);

    }

    /**
     * Displays an achievement dialog notification which can be then clicked to get full details.
     *
     * @param ach Achievement object to be displayed.
     */
    public void displayAchievement(Achievement ach){


        Intent i = new Intent(main_activity,AchievementDialogActivity.class);
        i.putExtra("achievement", ach);
        main_activity.startActivity(i);

    }

    /**
     * This method will draw a a list of trail segments to the trails map.
     *
     * @param segments ArrayList of segments to draw to screen
     */
    public void drawSegments(ArrayList<Segment> segments){
        for(Segment s:segments){
            drawSegment(s);
        }
    }

    /**
     * This method will draw a a list of trail segments to the trails map.
     *
     * @param seg Segement object to draw to the trails map.
     */
    public void drawSegment(Segment seg){

        int segmentId=seg.getSegmentId();

        String segmentName=seg.getSegmentName();

        int topLeftX=0;
        int topLeftY=0;

        //get x and y of where to draw segment on trails map

        if(inDevMode){
            if(segmentName.equals("ExecEnt_L21")){
                topLeftX=(int)segGraphicS1.getTopLeftX();
                topLeftY=(int)segGraphicS1.getTopLeftY();
            }else if(segmentName.equals("L20_L21")){
                topLeftX=(int)segGraphicS2.getTopLeftX();
                topLeftY=(int)segGraphicS2.getTopLeftY();
            }else if(segmentName.equals("L19_L20")){
                topLeftX=(int)segGraphicS3.getTopLeftX();
                topLeftY=(int)segGraphicS3.getTopLeftY();
            }else if(segmentName.equals("L18_L19")){
                topLeftX=(int)segGraphicS4.getTopLeftX();
                topLeftY=(int)segGraphicS4.getTopLeftY();
            }else if(segmentName.equals("DepeEnt_L18")){
                topLeftX=(int)segGraphicS5.getTopLeftX();
                topLeftY=(int)segGraphicS5.getTopLeftY();
            }
        }else{
            SegmentGraphic segGraphic = DummyDatabaseHelper.getInstance().getSegmentGraphic(segmentId);

            topLeftX=(int)segGraphic.getTopLeftX();
            topLeftY= (int)segGraphic.getTopLeftY();
        }

        //Segments are stored with upper and lowercase names in DB, but drawables are all lowercase - can this be changed
        segmentName = segmentName.toLowerCase();
        System.out.println("@DisplayManager.drawSegment segmentName is "+segmentName);//DeBug

        //look up segement in our drawables folder by graphics file name without extension
        int resId  = main_activity.getResources().getIdentifier(segmentName, "drawable", main_activity.getPackageName());
        //int resId  = main_activity.getResources().getIdentifier(segmentName, "drawable-nodpi-v4", main_activity.getPackageName());

        Drawable d = main_activity.getResources().getDrawable(resId);

        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();
        //Toast.makeText(main_activity,"w: "+width,Toast.LENGTH_SHORT).show();
        //Toast.makeText(main_activity,"h: "+height,Toast.LENGTH_SHORT).show();

        Bitmap segBitmap = BitmapFactory.decodeResource(main_activity.getResources(),  resId);
        canvas.drawBitmap(segBitmap, null, new Rect(topLeftX,topLeftY,topLeftX+width,topLeftY+height), null);

        //notify map there has been updates to redraws
        mapPanView.invalidate();
    }

    /**
     * Disable all dev mode marker (M) buttons from being clicked.
     */
    public void disableButtons(){
        Button markerButton = (Button) main_activity
                .findViewById(R.id.M1);
        markerButton.setClickable(false);

        markerButton = (Button) main_activity
                .findViewById(R.id.M2);
        markerButton.setClickable(false);

        markerButton = (Button) main_activity
                .findViewById(R.id.M3);
        markerButton.setClickable(false);

        markerButton = (Button) main_activity
                .findViewById(R.id.M4);
        markerButton.setClickable(false);

        markerButton = (Button) main_activity
                .findViewById(R.id.M5);
        markerButton.setClickable(false);


        markerButton = (Button) main_activity
                .findViewById(R.id.M6);
        markerButton.setClickable(false);
    }

    /**
     * Enable dev mode marker buttons to be clickable true
     */
    public void enableButtons(){
        Button markerButton = (Button) main_activity
                .findViewById(R.id.M1);
        markerButton.setClickable(true);

        markerButton = (Button) main_activity
                .findViewById(R.id.M2);
        markerButton.setClickable(true);

        markerButton = (Button) main_activity
                .findViewById(R.id.M3);
        markerButton.setClickable(true);

        markerButton = (Button) main_activity
                .findViewById(R.id.M4);
        markerButton.setClickable(true);

        markerButton = (Button) main_activity
                .findViewById(R.id.M5);
        markerButton.setClickable(true);

        markerButton = (Button) main_activity
                .findViewById(R.id.M6);
        markerButton.setClickable(true);
    }

    /**
     * Draws and zooms into an achievement green marker on map at the given location.
     *
     * @param loc Location to draw achievement
     * @param zoomIntoMarker true if map should zoom into marker for this achievement
     * @param isCurrentLocation true will draw current marker, false draws solid marker
     */
    public void drawAchievementMarker(Location loc, Boolean zoomIntoMarker,Boolean isCurrentLocation){

        if(isCurrentLocation){
            //draw current marker
            drawMarker(loc,zoomIntoMarker,achievementMarkerOuterColor,markerOuterRadius,markerInnerColor,markerInnerRadius);
            if(previousLoc!=null){
                //draw over previous marker
                drawMarker(previousLoc,false,previousOuterColor,markerOuterRadius,previousOuterColor,markerOuterRadius);
            }
        }else{
            //only draw current marker
            drawMarker(loc,zoomIntoMarker,achievementMarkerOuterColor,markerOuterRadius,achievementMarkerOuterColor,markerOuterRadius);
        }
        //keep color/location for recoloring on next marker/achievement where needed
        previousOuterColor=achievementMarkerOuterColor;
        previousLoc=loc;
    }

    /**
     *     * Draws and zooms into an general red marker on map at the given location.
     *
     * @param loc Location to draw achievement
     * @param zoomIntoMarker true if map should zoom into marker for this achievement
     * @param isCurrentLocation true will draw current marker, false draws solid marker
     */
    public void drawMarker(Location loc, Boolean zoomIntoMarker,Boolean isCurrentLocation){


        if(isCurrentLocation){
            //draw current marker
            drawMarker(loc,zoomIntoMarker,markerOuterColor,markerOuterRadius,markerInnerColor,markerInnerRadius);
            if(previousLoc!=null){
                //draw over previous marker
                drawMarker(previousLoc,false,previousOuterColor,markerOuterRadius,previousOuterColor,markerOuterRadius);
            }

        }else{
            //only draw current marker
            drawMarker(loc,zoomIntoMarker,markerOuterColor,markerOuterRadius,markerOuterColor,markerOuterRadius);
        }
        //keep color/location for recoloring on next marker/achievement where needed
        previousOuterColor=markerOuterColor;
        previousLoc=loc;
    }

    /**
     * Draws and zooms into the marker given the color attributes.
     *
     * @param loc Location to draw marker
     * @param zoomIntoMarker true if map should zoom into marker for this achievement
     * @param colorOuter outer fill color for marker circle
     * @param radiusOuter radius for outer marker circle
     * @param colorInner inner fill color for marker circle
     * @param radiusInner radius for inner marker circle
     */
    public void drawMarker(Location loc, Boolean zoomIntoMarker,int colorOuter,int radiusOuter,int colorInner,int radiusInner) {
        float x = loc.getX();
        float y = loc.getY();


        //zoom into map
        if(zoomIntoMarker){

            DoubleTapZoom   doubleTap = mapPanView.new DoubleTapZoom(markerZoomLevel, false,
                    x, y, loc,colorOuter,markerOuterRadius,colorInner,markerInnerRadius,
                    mapPanView);

            //TouchImageView recalls the drawMarker method after it finishes zooming
            mapPanView.compatPostOnAnimation(doubleTap);
        }else{
            //paint outer marker
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(colorOuter);

            // canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            // paint.setColor(Color.parseColor("#CD5C5C"));

            canvas.drawCircle(x, y, radiusOuter, paint);

            // if inner and outer colors are different draw inner circle
            if(radiusInner!=radiusOuter){
                paint.setColor(colorInner);
                canvas.drawCircle(x, y, radiusInner, paint);
            }

            //notify TouchImageView to redraw itself
            mapPanView.invalidate();

        }
    }

    /**
     * OLD KEEPING FOR REFERENCE
     */
    public void updateTrailsMap(ArrayList<String> currentPathSegments) {
//        // //currentPathSegments=DistanceManager.getInstance().getPathSegments();
//        int numberOfPaths = currentPathSegments.size();
//
//        if (!onTrailsServiceRunning) {
//            // flag service is started/begin started
//            onTrailsServiceRunning = true;
//            // begin populating distance, pace, time on trails
//            beginGatheringTime();
//            // start the onTrailsService
//            Intent intent = new Intent(main_activity, OnTrailsService.class);
//            main_activity.startService(intent);
//        }
//
//        if (currentPathSegments.size() == 1) {
//            //show labels on first scan
//            TextView timeLabel = (TextView) main_activity
//                    .findViewById(R.id.TimeLabelText);
//            TextView distanceLabel = (TextView) main_activity
//                    .findViewById(R.id.DistanceLabelText);
//            TextView paceLabel = (TextView) main_activity
//                    .findViewById(R.id.PaceLabelText);
//
//            timeLabel.setVisibility(View.VISIBLE);
//            distanceLabel.setVisibility(View.VISIBLE);
//            paceLabel.setVisibility(View.VISIBLE);
//
//            totalDistance.setText(DistanceManager.getInstance().getDistance()
//                    + " " + DISTANCE_UNITS);
//            currentPace.setText("on next QR code! Better hurry :)");
//        } else {
//            // update both pace and time for all other scenarios
//            totalDistance.setText(DistanceManager.getInstance().getDistance()
//                    + " " + DISTANCE_UNITS);
//            currentPace.setText(DistanceManager.getInstance().getPace() + " "
//                    + PACE_UNITS);
//        }
//
    }

    /**
     *
     * @return true if trails service is running collecting statistics
     */
    public boolean isOnTrailsServiceRunning() {
        return onTrailsServiceRunning;
    }

    /**
     * Convert seconds to string format to include hours and minutes.
     *
     * @param totalSecondsDouble seconds to convert to string format
     * @return string format hh:mm:ss
     */
    public String convertSecondsToTimeString(double totalSecondsDouble) {
        String resultString;
        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int totalSeconds = (int) Math.round(totalSecondsDouble);
        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        String minuteStr=Integer.toString(minutes);
        String secondsStr=Integer.toString(seconds);
        if(secondsStr.length()<2){
            secondsStr="0"+secondsStr;
        }

        if (hours > 0) {
            if(minuteStr.length()<2){
                resultString = hours + ":" +"0"+ minutes + ":" + secondsStr;
            }else{
                resultString = hours + ":"+ minutes + ":" + secondsStr;
            }
        } else if (minutes > 0) {
            //single digit minutes
            resultString = minutes + ":" + secondsStr;
        } else {
            resultString = "00" + ":" + secondsStr;
        }

        return resultString;
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * Hide all dev mode buttons setting them to VIEW.GONE
     */
    public void hideDevButtons(){
        Button btn = (Button) main_activity
                .findViewById(R.id.M1);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.M2);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.M3);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.M4);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.M5);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.M6);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.S1);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.S2);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.S3);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.S4);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.S5);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.A1);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.A2);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.A3);
        btn.setVisibility(View.GONE);

        btn = (Button) main_activity
                .findViewById(R.id.ClearButton);
        btn.setVisibility(View.GONE);
    }

    /**
     * Show all dev mode buttons setting them to VIEW.VISIBLE
     */
    public void showDevButtons(){

        Button btn = (Button) main_activity
                .findViewById(R.id.M1);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.M2);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.M3);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.M4);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.M5);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.M6);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.S1);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.S2);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.S3);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.S4);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.S5);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.A1);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.A2);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.A3);
        btn.setVisibility(View.VISIBLE);

        btn = (Button) main_activity
                .findViewById(R.id.ClearButton);
        btn.setVisibility(View.VISIBLE);
    }

    /**
     * Get most recent copied link copied to the clipboard onLongPress in TouchImageView
     *
     * @return string link to be sent via sms for sharing locations
     */
    public String getLongPressLink() {
        return longPressLink;
    }

    /**
     * Sets the link that will be used for any calls to send sms location
     *
     * @param longPressLink link to be sent via sms for sharing trail location
     */
    public void setLongPressLink(String longPressLink) {
        this.longPressLink = longPressLink;
    }

    /**
     * Set boolean to track if user is in dev mode.
     *
     * @param bool true if in dev mode, false if not
     */
    public void setInDevMode(Boolean bool){
        this.inDevMode=bool;
    }

    /**
     * Get boolean to determine if user is in dev mode.
     *
     * @return true if in dev mode, false if not in dev mode
     */
    public Boolean getInDevMode(){
        return this.inDevMode;
    }


    /**
     * Draws a new trails map over the current one set in the trails map view.
     */
    public void clearTrailsMap(){
        previousLoc=null;

        int mapX=0;
        int mapY=0;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap1 = BitmapFactory.decodeResource(main_activity.getResources(), R.drawable.trails_nomarkup, options);
        canvas.drawBitmap(bitmap1, null, new Rect(mapX,mapY,mapWidth,mapHeight), null);
        bitmap1.recycle();
        mapPanView.invalidate();


        Toast.makeText(main_activity, "Map Cleared.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates alert showing app version and release date information.
     */
    public void showVersionDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(main_activity);

        try{
            String versionName = main_activity.getPackageManager().getPackageInfo(main_activity.getPackageName(), 0).versionName;
            String appFullName=main_activity.getString(R.string.app_full_name);
            String releaseDate=main_activity.getString(R.string.release_date);

            builder.setMessage(appFullName+"\n\nRelease Date: "+releaseDate+"\nVersion: Alpha "+versionName)
                    .setCancelable(false)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

        }catch(PackageManager.NameNotFoundException e){
            Toast.makeText(main_activity.getApplicationContext(), "Unable to gather version info.", Toast.LENGTH_LONG).show();

        }


    }

    /**
     * Shows a loading dialog to the user until dismissLoadingDialog is called.
     */
    public void showLoadingDialog(){

        if(progressDialog!=null){
            progressDialog.show();
        }else{
            progressDialog=new ProgressDialog(main_activity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading. Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

    }

    /**
     * Dismiss a Loading dialog that is showing to the user.
     */
    public void dismissLOadingDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    /**
     *
     * @return Bitmap associcated with the current trails map (could have drawings on it)
     */
    public Bitmap getCanvasBitmap(){
        return drawnBitmap;
    }
}