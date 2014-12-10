package com.tcsorcs.trailsapp.managers;


import com.tcsorcs.trailsapp.R;
import android.media.MediaPlayer;

public class SoundManager {

	public static SoundManager getInstance() {
		return SoundManager.instance;
	}

	static SoundManager instance = new SoundManager();
	
	public SoundManager(){
		
	}
	
	public boolean playSound(String name){
		MediaPlayer mediaPlayer = MediaPlayer.create(GeneralManager.getInstance().main_activity, R.raw.hooray);
		mediaPlayer.start();
		return true;
	}
}
