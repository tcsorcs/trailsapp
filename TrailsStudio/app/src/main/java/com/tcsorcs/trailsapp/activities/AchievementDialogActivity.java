package com.tcsorcs.trailsapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.helpers.Achievement;

public class AchievementDialogActivity extends Activity {

    private Handler handler  = null; //used for delay on closing achievement dialog
    private Runnable runnable=null; //used for delay on closing achievement dialog

    private final int DIALOG_TIME=4000; //time to display achievement dialog before auto closing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set layout for achievement dialog
        setContentView(R.layout.activity_achievement_dialog);


        //get achievement object passed in
        Bundle data = getIntent().getExtras();
        final Achievement ach =data.getParcelable("achievement");

        if(ach!=null){

            TextView text = (TextView) findViewById(R.id.achieveDialogTitle);
            //set achievement title
            text.setText(ach.getName());
            text = (TextView) findViewById(R.id.achieveDialogDescription);
            //set achievement description
            text.setText(ach.getShortDesc());

            ImageView image = (ImageView) findViewById(R.id.image);
            //look up image for achievement in drawables folder by image name
            int resId  = getResources().getIdentifier(ach.getIcon(), "drawable", getPackageName());
            image.setImageResource(resId);


            RelativeLayout relLay=(RelativeLayout)findViewById(R.id.AchievementDialogView);
            //set click listener for achievement dialog
            relLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    //pass achievement object on to AchievementDetailsActivity
                    Intent i = new Intent(getApplicationContext(),AchievementDetailsActivity.class);
                    i.putExtra("achievement", ach);
                    startActivity(i);
                }
            });

            //if user clicks outisde of achievement dialog, close the dialog
            RelativeLayout relLayBackground=(RelativeLayout)findViewById(R.id.AchievementDialogBackgroundView);
            relLayBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });




            //start timer thread to close dialog after set time
            handler  = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            };
            handler.postDelayed(runnable, DIALOG_TIME);

        }else{
            //could not find achievement object, finish activity
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //if activity paused, remove timer thread and finish dialog
        handler.removeCallbacks(runnable);
        finish();
    }

    @Override
    public void onBackPressed() {
        //close achievement dialog when back button is pressed
        finish();
    }

}
