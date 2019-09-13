package com.example.meivents;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        String eventTitle = extras.getString("TITLE");
        String eventDate = extras.getString("DATE");
        String eventTime = extras.getString("TIME");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0 );

        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Erinnerung")
                .setContentTitle(eventTitle);

        Date date = formatToDate(eventDate);
        if(eventIsToday(date)){
            notBuilder.setContentText("Heute um " + eventTime);
        } else {
            notBuilder.setContentText("Morgen um " + eventTime);
        }

        notBuilder.setContentIntent(pendingIntent);
        notBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        notBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notBuilder.build());

    }

    //formats String date to date object
    private Date formatToDate(String date){
        try {
            DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN);
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //checks if event may be added today and also takes place today
    private boolean eventIsToday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = calendar.getTime();
        return !date.after(today);
    }
}
