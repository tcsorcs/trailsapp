package com.tcsorcs.trailsapp.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.managers.DisplayManager;


public class GoogleMapsActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {


  private BitmapDescriptor image;

    private GroundOverlay mGroundOverlay;

    //southwest corner of trail map
    private LatLng southwest= new LatLng(39.146086,-84.255041);

    //northeast corner of trail map
    private LatLng northeast= new LatLng(39.157627,-84.233797);


    private  GoogleMap googleMap; //google map object

    private SeekBar mTransparencyBar; //dev mode bar to adjust transparency

    private static final int TRANSPARENCY_MAX = 100; //100 is fully transparent


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set layout for activity
        setContentView(R.layout.activity_google_maps);


        // Getting reference to the google map fragment
        MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.googlemap);

        // Getting GoogleMap object from the fragment
        googleMap = fm.getMap();

        // googleMap is a GoogleMap object
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //zoom to TCS coordinates on start
        LatLng  myLocation = new LatLng(39.15,-84.244493);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,16));

        //check for GPS enabled
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showEnableGPSDialog();
        }


        //set listener for if My Location button is clicked to check for GPS again
        final Context context = this;
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_SHORT).show();
                    showEnableGPSDialog();
                }
                return false;
            }
        });


            //draw trails map over Google Maps
        drawImageOverMap();

        //initialize transparency adjuster bar for dev mode
        mTransparencyBar = (SeekBar) findViewById(R.id.transparencySeekBar);
        mTransparencyBar.setMax(TRANSPARENCY_MAX);
        mTransparencyBar.setProgress(0);

        mTransparencyBar.setOnSeekBarChangeListener(this);

        //only show transparency bar if in dev mode
        if(DisplayManager.getInstance().getInDevMode()){
            mTransparencyBar.setVisibility(View.VISIBLE);
        }else{
            mTransparencyBar.setVisibility(View.GONE);

        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mGroundOverlay != null) {
            mGroundOverlay.setTransparency((float) progress / (float) TRANSPARENCY_MAX);
        }
    }

    private void drawImageOverMap(){

        //BitmapFactory.Options options = new BitmapFactory.Options();
       //options.inSampleSize = 2;
      // Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.trails_nomarkup,options);

        Bitmap bitmap=DisplayManager.getInstance().getCanvasBitmap();

        image= BitmapDescriptorFactory.fromBitmap(bitmap);
        LatLngBounds bounds =new LatLngBounds( southwest,  northeast);


        mGroundOverlay  = googleMap.addGroundOverlay(new GroundOverlayOptions()
                .image(image)
                .transparency(0)
                .positionFromBounds(bounds));


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_google_maps, menu);

        //return true to add menu to this activity
        return false;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     *  Shows a dialog to the user with button to direct them
     *  to their settings screen to turn on GPS.
     */
    private void showEnableGPSDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("For best accuracy, please enable Location services (GPS) on your device when using the Where Am I feature. This can be found under settings.")
                .setCancelable(false)
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }




}
