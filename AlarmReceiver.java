package com.example.meivents;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        String eventTitle = extras.getString("TITLE");
        String eventTime = extras.getString("TIME");


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0 );

        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Erinnerung")
                .setContentText("Morgen findet um " + eventTime + " das Event \"" + eventTitle + "\" statt!")
                .setTicker("Erinnerung")
                .setSmallIcon(R.drawable.logo_jpg);

        notBuilder.setContentIntent(pendingIntent);
        notBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        notBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notBuilder.build());
    }
}
