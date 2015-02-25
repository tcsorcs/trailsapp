package com.tcsorcs.trailsapp.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.tcsorcs.trailsapp.activites.MainTrailsActivity;
import com.tcsorcs.trailsapp.R;

public class OnTrailsService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int NOTIFICATION_ID_FOREGROUND = 1;

		// build notification intent to direct notification where to go when
		// pressed
		Intent notificationIntent = new Intent(this, MainTrailsActivity.class);
		//notificationIntent.setAction(Intent.ACTION_MAIN);
		//notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		// TODO
		// set icon for notification
		Bitmap icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);

		// build notification
		Notification notification = new NotificationCompat.Builder(this)
				.setContentTitle(
						getResources().getString(R.string.notification_title))
				.setTicker(getResources().getString(R.string.ticker_text))
				.setContentText(
						getResources().getString(R.string.notification_message))
				.setSmallIcon(R.drawable.trails_notification)
				.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
				.setContentIntent(pendingIntent).setOngoing(true).build();
		startForeground(NOTIFICATION_ID_FOREGROUND, notification);

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Used only in case of bound services.
		return null;
	}
}