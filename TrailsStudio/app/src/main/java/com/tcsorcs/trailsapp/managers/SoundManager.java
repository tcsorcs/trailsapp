package com.tcsorcs.trailsapp.managers;


import com.tcsorcs.trailsapp.R;
import android.media.MediaPlayer;

public class SoundManager {
    private MediaPlayer mediaPlayer=null;

    public static SoundManager getInstance() {
        return SoundManager.instance;
    }

    static SoundManager instance = new SoundManager();

    public SoundManager(){

    }

    public boolean playSound(String name){
        mediaPlayer=new MediaPlayer();
        mediaPlayer = MediaPlayer.create(DisplayManager.getInstance().main_activity, R.raw.hooray);
        mediaPlayer.start();
        return true;
    }

    public boolean playHooray(String name){
        mediaPlayer=new MediaPlayer();
        mediaPlayer = MediaPlayer.create(DisplayManager.getInstance().main_activity, R.raw.hooray);
        mediaPlayer.start();
        return true;
    }
    public boolean playKing(String name){
        mediaPlayer=new MediaPlayer();
        mediaPlayer = MediaPlayer.create(DisplayManager.getInstance().main_activity, R.raw.kinginthenorth);
        mediaPlayer.start();
        return true;
    }
    public boolean playKirby(String name){
        mediaPlayer=new MediaPlayer();
        mediaPlayer = MediaPlayer.create(DisplayManager.getInstance().main_activity, R.raw.kirby);
        mediaPlayer.start();
        return true;
    }
}
