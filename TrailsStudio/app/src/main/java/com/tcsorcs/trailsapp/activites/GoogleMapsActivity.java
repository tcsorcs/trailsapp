package com.tcsorcs.trailsapp.activites;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.tcsorcs.trailsapp.R;

public class GoogleMapsActivity extends ActionBarActivity {

    private  GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);



        // Getting reference to the SupportMapFragment of activity_main.xml
        MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.googlemap);

        // Getting GoogleMap object from the fragment
        googleMap = fm.getMap();

        // map is a GoogleMap object
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //zoom to TCS
        LatLng  myLocation = new LatLng(39.15,-84.244493);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,16));


        //zoom to users current location
//        Location location = googleMap.getMyLocation();
//        LatLng myLocation;
//        if (location != null) {
//            myLocation = new LatLng(location.getLatitude(),
//                    location.getLongitude());
//
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,16));
//
//        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
