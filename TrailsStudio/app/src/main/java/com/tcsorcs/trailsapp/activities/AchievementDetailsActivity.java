package com.tcsorcs.trailsapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tcsorcs.trailsapp.R;
import com.tcsorcs.trailsapp.helpers.Achievement;

public class AchievementDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //set layout for this fragment
		setContentView(R.layout.activity_achievement_details);

        //get achievement object passed in
        Bundle data = getIntent().getExtras();
        final Achievement ach =data.getParcelable("achievement");

        if(ach!=null){

            TextView text = (TextView) findViewById(R.id.achieveDetailsTitle);
            //set achievement title
            text.setText(ach.getName());
            text = (TextView) findViewById(R.id.achieveDetailsDescription);
            //set achievement description
            text.setText(ach.getShortDesc()+" "+ach.getDesc());

            text = (TextView) findViewById(R.id.achieveDetailsDate);
            //set achievement date
            text.setText(ach.getDateAchieved());

            ImageView image = (ImageView) findViewById(R.id.achievementDetailsImage);
            //look up image in drawables folder for this achievement by image name
            int resId  = getResources().getIdentifier(ach.getIcon(), "drawable", getPackageName());

            image.setImageResource(resId);

            ImageView closeImage=(ImageView)findViewById(R.id.achievementDetailsCloseButton);
            //set click listener for close button
            closeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });
        }else{
            //finish activity if achievement object cannot be parsed
            finish();
        }


	}


	@Override
	public void onBackPressed() {
		//finish and return to previous activity
		finish();
	}

}
