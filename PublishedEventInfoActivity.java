package com.example.meivents;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PublishedEventInfoActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener {

    TextView titleTextView, dateTextView, timeTextView, placeTextView, descrTextView;
    Switch reminderSwitch;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    String eventId, userId, title, date, time, place, admission, descr, status;
    boolean reminderOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_event_info);

        titleTextView = findViewById(R.id.title_published_textView);
        dateTextView = findViewById(R.id.date_published_textView);
        timeTextView = findViewById(R.id.time_published_textView);
        placeTextView = findViewById(R.id.place_published_textView);
        descrTextView = findViewById(R.id.descr_published_textView);
        reminderSwitch = findViewById(R.id.reminder_switch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();


        reminderOn = false;
        DatabaseReference remindRef = rootRef.child(firebaseAuth.getUid()).child("Events with Reminder");
        Query query = remindRef.orderByChild("eventId").equalTo(eventId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        System.out.println(reminderOn);

        reminderSwitch.setChecked(reminderOn);
        reminderSwitch.setOnCheckedChangeListener(this);


        Bundle extras = getIntent().getExtras();
        userId = extras.getString("USERID");
        eventId = extras.getString("EVENTID");
        title = extras.getString("TITLE");
        date = extras.getString("DATE");
        time = extras.getString("TIME");
        place = extras.getString("PLACE");
        admission = extras.getString("ADMISSION");
        descr = extras.getString("DESCR");
        status = extras.getString("STATUS");


        titleTextView.setText(title);
        dateTextView.setText(date);
        timeTextView.setText(time);
        placeTextView.setText(place);
        setDescrTextView(descr);

    }

    private void setDescrTextView(String descr){
        if(!descr.isEmpty()){
            descrTextView.setText(descr);
        } else {
            descrTextView.setText("keine weiteren Details");
            descrTextView.setTypeface(null, Typeface.ITALIC);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Event eventWithReminder = new Event(userId, eventId, title, date, time, place, admission, descr, status);
        if(isChecked){
            reminderSwitch.setChecked(true);
            reminderOn = true;
            firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Events with Reminder").child(eventWithReminder.getEventId()).setValue(eventWithReminder);
        }
        if(!isChecked) {
            reminderSwitch.setChecked(false);
            reminderOn = false;
            DatabaseReference eventRemRef = firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Events with Reminder").child(eventWithReminder.getEventId());
            eventRemRef.removeValue();
        }

    }

    public void setAlarm(String eventId, String eventTitle, String eventDate, String eventTime){

        Long alertTime = System.currentTimeMillis() + 5000;

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("TITLE", eventTitle);
        intent.putExtra("TIME", eventTime);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

}
