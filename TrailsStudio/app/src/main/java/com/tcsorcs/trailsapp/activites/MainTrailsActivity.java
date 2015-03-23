package com.tcsorcs.trailsapp.activites;

import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.helpers.Location;
import com.tcsorcs.trailsapp.fragments.AchievementHistoryListFragment;
import com.tcsorcs.trailsapp.fragments.NavigationDrawerFragment;
import com.tcsorcs.trailsapp.fragments.OrcContactListFragment;
import com.tcsorcs.trailsapp.fragments.TrailBreakdownFragment;
import com.tcsorcs.trailsapp.managers.DisplayManager;
import com.tcsorcs.trailsapp.managers.DistanceManager;
import com.tcsorcs.trailsapp.managers.GeneralManager;
import com.tcsorcs.trailsapp.managers.InputManager;
import com.tcsorcs.trailsapp.mapview.TouchImageView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;


public class MainTrailsActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private Handler qrCodeHandler = new Handler(); //used for timer delay before zooming into map
    private String trailQRCodeContents = null; //holds contents of qrcode
    private String trailsLocSize=null; //number of incoming sms locations to draw
    private String trailsLocX=null; // incoming sms x coordinate
    private String trailsLocY=null; // incoming sms y coordinate
    private boolean trailQRScanFound = false; // keep track if qr code is found
    private boolean trailsLocFound = false; // keep track if incoming sms location is found


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_trails);


        GeneralManager.getInstance().main_activity = this;


        DisplayManager.instance = new DisplayManager();
        DisplayManager.getInstance().main_activity = GeneralManager
                .getInstance().main_activity;


        // set onClicks for Buttons
        DisplayManager.getInstance().setButtonCallbacks();
        // set initial map image/zoom and paint map
        DisplayManager.getInstance().initializeMapView();

        //hide dev options on first creating activity
        DisplayManager.getInstance().hideDevButtons();
        DisplayManager.getInstance().setInDevMode(false);

        DistanceManager.instance = new DistanceManager();


        //initialize navigation drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));




        //check for incoming sms before splash screen, otherwise onNewIntent handles
        //incoming sms since activity is already created
        Uri uri = getIntent().getData();
        if (uri != null) {
            //paramenter scanned_data given by QR Code Reader documentation
            //https://scan.me/help#/article/1363347
            trailQRCodeContents = uri.getQueryParameter("scanned_data");
            if (trailQRCodeContents != null) {
                trailQRScanFound = true;
            }

            //TODO implement sending multiple locations using size parameter

            trailsLocSize = uri.getQueryParameter("size");
            if (trailsLocSize != null) {
                trailsLocFound = true;
                trailsLocX = uri.getQueryParameter("x");
                trailsLocY = uri.getQueryParameter("y");
            }
        }


    }


    @Override
    public void onBackPressed() {


        if (mNavigationDrawerFragment.isDrawerOpen()) {
            //close drawer on back press
            mNavigationDrawerFragment.closeDrawer();
        } else {
            //return to previous fragment
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                //home screen set title
                if (fm.getBackStackEntryCount() == 1) {
                    mTitle = getString(R.string.title_trails);
                    restoreActionBar();
                }
                fm.popBackStack();
            } else {
                // go to home screen when the back button is pressed with no fragments on stack
                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                setIntent.addCategory(Intent.CATEGORY_HOME);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setIntent);
            }
        }


    }

    @Override
    public void onPause() {
        super.onPause(); // Always call the superclass method first

        // whenever our display is hidden, stop updating textviews pace,
        // distance, time on trails
        DisplayManager.getInstance().stopGatheringTime();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //  Toast.makeText(getApplicationContext(), "onNewIntent Called", Toast.LENGTH_LONG).show();

        //check to see if qrcode was scanned
        Uri uri = intent.getData();
        if (uri != null) {
            //paramenter scanned_data given by QR Code Reader documentation
            //https://scan.me/help#/article/1363347
            trailQRCodeContents = uri.getQueryParameter("scanned_data");
            if (trailQRCodeContents != null) {
                trailQRScanFound = true;
            }
            trailsLocSize = uri.getQueryParameter("size");
            if (trailsLocSize != null) {
                trailsLocFound = true;
                trailsLocX = uri.getQueryParameter("x");
                trailsLocY = uri.getQueryParameter("y");
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first

        // whenever our main display is brought back to the foreground, continue
        // updating textviews pace, distance, time on trails if service is running
        if (DisplayManager.getInstance().isOnTrailsServiceRunning()) {
            DisplayManager.getInstance().beginGatheringTime();
        }

        //on Resuming, if resuming from a qr code scan, update the location on map and zoom
//        // send qr contents to inputmanager if found, pausing 3 seconds
//        // to allow for mapview to load and show transition to new location
//        // without the 3 second delay it just goes directly to the location
        if (trailQRScanFound) {
            Toast.makeText(getApplicationContext(), trailQRCodeContents, Toast.LENGTH_LONG).show();
            // pass qr code contents to input manager
            qrCodeHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputManager.getInstance().inputQRC(trailQRCodeContents);

                }
            }, 3000);
        }

        if (trailQRScanFound) {
            // reset flag for future scans
            trailQRScanFound = false;

        }

        if (trailsLocFound) {

            //return to main trails activity map view, popping all fragment screens off the stack
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            mTitle = getString(R.string.title_trails);

            //Toast.makeText(getApplicationContext(), "X:"+trailsLocX, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "Y:"+trailsLocY, Toast.LENGTH_LONG).show();

            //draw markers from sms location
            qrCodeHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //  InputManager.getInstance().inputQRC(trailQRCodeContents);

                    if (trailsLocX != null && trailsLocY != null) {
                        int x = Integer.parseInt(trailsLocX);
                        int y = Integer.parseInt(trailsLocY);

                        Location loc=new Location(x,y);
                        DisplayManager.getInstance().drawMarker(loc,true,true);

                    }

                }
            }, 2000);
        }

        if (trailsLocFound) {
            // reset flag for future scans
            trailsLocFound = false;

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // end notification service that shows trails app is running
        DisplayManager.getInstance().stopOnTrailsService();

        TouchImageView mapView = (TouchImageView) this
                .findViewById(R.id.MapPanView);

        mapView.setImageBitmap(null);
    }


    //TODO new instances created of each fragment every time selected, need to just have one instance and switch to that instance
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        //pop everything off fragment stack if select home screen from drawer
        if (position == 0) {
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            mTitle = getString(R.string.title_trails);


        }else if (position == 1) {
            //Google Maps selected from Navigation Drawer
            Intent i = new Intent(this,GoogleMapsActivity.class);
            startActivity(i);

        } else if (position == 2) {
            //AchievementFragment selected from navigation drawer
            //place on stack
            fragmentManager.beginTransaction()
                    .replace(R.id.MainTrailsView, AchievementHistoryListFragment.newInstance("1", "2"), "AchievementList_Frag")
                    .addToBackStack(null).commit();
            mTitle =  getString(R.string.title_achievements);
        } else if (position == 3) {
            //ORCs Lounge selected from navigation drawer
            //place on stack
            fragmentManager.beginTransaction()
                    .replace(R.id.MainTrailsView, OrcContactListFragment.newInstance("1", "2"), "OrcContactList_Frag")
                    .addToBackStack(null).commit();
            mTitle = getString(R.string.title_orcslounge);

        } else if (position == 4) {
            //TrailsBreakdown selected from navigation drawer
            //place on stack
            fragmentManager.beginTransaction()
                    .replace(R.id.MainTrailsView, TrailBreakdownFragment.newInstance("1", "2"), "TrailBreakdown_Frag")
                    .addToBackStack(null).commit();
            mTitle = getString(R.string.title_trailbreakdown);

        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main_trails, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "To be developed.", Toast.LENGTH_SHORT).show();
            return true;
        }


        if (item.getItemId() == R.id.toggle_dev) {

            if(DisplayManager.getInstance().getInDevMode()){
                DisplayManager.getInstance().hideDevButtons();
                DisplayManager.getInstance().setInDevMode(false);
                Toast.makeText(this, "Dev Mode Off", Toast.LENGTH_SHORT).show();

            }else{

                DisplayManager.getInstance().showDevButtons();
                DisplayManager.getInstance().setInDevMode(true);
                Toast.makeText(this, "Dev Mode On", Toast.LENGTH_SHORT).show();

            }

            return true;
        }

        if (item.getItemId() == R.id.exit_menu) {

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    // stop service if it is running
                                    DisplayManager.getInstance().stopOnTrailsService();

                                    DisplayManager.getInstance().stopGatheringTime();
                                    // ((BitmapDrawable)mapPanView.getDrawable()).getBitmap().recycle();

                                    // exit our trails main activity
                                    // application
                                    DisplayManager.getInstance().main_activity.finish();
                                }
                            }).setNegativeButton("No", null).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //do whatever you want the back key to do
                    dialog.dismiss();
                }
            }).show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}