package com.tcsorcs.trailsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.tcsorcs.trailsapp.R;

public class SplashScreenActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000; // time to keep splash screen
    private Activity splash_activity; // visible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splash_activity = this;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over

                // go to main activity, this splash screen activity is destroyed
                // in the onDestroy method below
                Intent i = new Intent(splash_activity, MainTrailsActivity.class);
                splash_activity.startActivity(i);

            }
        }, SPLASH_TIME_OUT);

    }



    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
        // destroy splash screen activity
        ((ImageView) this.findViewById(R.id.splashScreenImage))
                .setImageBitmap(null);
        finish();
    }

}
