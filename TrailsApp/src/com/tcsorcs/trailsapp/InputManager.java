package com.tcsorcs.trailsapp;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class InputManager {
  public static InputManager getInstance() {
	  return InputManager.instance;
  }
  static InputManager instance = new InputManager();
  
  public void inputQRC(String qrc, LinearLayout layout){
	  if(qrc.endsWith("/achievements/executive")) {
			ImageView i = new ImageView(DisplayManager.getInstance().main_activity);
			// Display the achievement.
			// i.setImageDrawable(getResources().getDrawable(R.drawable.achievement));
			layout.removeAllViews();
			layout.addView(i);
		}
		else {
			TextView t = new TextView(DisplayManager.getInstance().main_activity);
			layout.removeAllViews();
			t.setText("Unknown barcode: "+qrc);
			layout.addView(t);
		}
  }
}
