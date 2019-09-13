package com.example.meivents;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


//Activity that gives all added information to event by clicking on list item
//plus opportunity to set an alarm on events

public class PublishedEventInfoActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener {

    TextView titleTextView, dateTextView, timeTextView, placeTextView, descrTextView;
    Switch reminderSwitch;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    String userId, eventId, title, date, time, place, admission, descr, status;


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

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();

        reminderSwitch.setChecked(false);

        DatabaseReference remindRef = rootRef.child(firebaseAuth.getUid()).child("Events with Reminder");
        Query query = remindRef.orderByChild("eventId").equalTo(eventId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Event event = snapshot.getValue(Event.class);
                        if(event != null){
                            reminderSwitch.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reminderSwitch.setOnCheckedChangeListener(this);


        remindRef.addChildEventListener(new ChildEventListener() {

            //sets alarm for events with reminder switch on if there isn't already set an alarm
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event = dataSnapshot.getValue(Event.class);
                if(PendingIntent.getBroadcast(getApplicationContext(), event.getEventId().hashCode(),
                        new Intent(getApplicationContext(), AlarmReceiver.class),
                        PendingIntent.FLAG_NO_CREATE) == null){
                    setAlarm(event.getEventId(), event.getTitle(), event.getDate(), event.getTime());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            //cancels alarm for events with reminder switch changed to off again
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                cancelAlarm(event.getEventId());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setDescrTextView(String descr){
        if(!descr.isEmpty()){
            descrTextView.setText(descr);
        } else {
            descrTextView.setText("keine weiteren Details");
            descrTextView.setTypeface(null, Typeface.ITALIC);
        }
    }

    //adds or removes event to firebase depending on whether reminder switch is checked or not
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Event eventWithReminder = new Event(userId, eventId, title, date, time, place, admission, descr, status);
        if(isChecked){
            firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Events with Reminder").child(eventWithReminder.getEventId()).setValue(eventWithReminder);
        }
        if(!isChecked) {
            DatabaseReference eventRemRef = firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Events with Reminder").child(eventWithReminder.getEventId());
            eventRemRef.removeValue();
        }

    }

    //sets alarm the day before event takes place with unique integer id depending on eventID
    public void setAlarm(String eventId, String eventTitle, String eventDate, String eventTime){
        int alarmId = eventId.hashCode();

        Date date = formatToDate(eventDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        long alarmTime = calendar.getTimeInMillis();

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("TITLE", eventTitle);
        alarmIntent.putExtra("DATE", eventDate);
        alarmIntent.putExtra("TIME", eventTime);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }


    //cancels alarm using alarmID when reminder switch is not active anymore
    public void cancelAlarm(String eventId){
        int alarmId = eventId.hashCode();

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

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

}
