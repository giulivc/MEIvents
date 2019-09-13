package com.example.meivents;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Activity that gives all added information to event by clicking on list item
//plus information about processing status

public class SharedEventInfoActivity extends AppCompatActivity {

    ImageView statusImage;
    TextView statusTextView, titleTextView, dateTextView, timeTextView, placeTextView, descrTextView;

    String status, eventId, title, date, time, place, descr;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_event_info);

        statusImage = findViewById(R.id.status_shared_imageView);
        statusTextView = findViewById(R.id.status_shared_textView);

        titleTextView = findViewById(R.id.title_shared_textView);
        dateTextView = findViewById(R.id.date_shared_textView);
        timeTextView = findViewById(R.id.time_shared_textView);
        placeTextView = findViewById(R.id.place_shared_textView);
        descrTextView = findViewById(R.id.descr_shared_textView);

        Bundle extras = getIntent().getExtras();
        eventId = extras.getString("EVENTID");
        title = extras.getString("TITLE");
        date = extras.getString("DATE");
        time = extras.getString("TIME");
        place = extras.getString("PLACE");
        descr = extras.getString("DESCR");
        status = extras.getString("STATUS");

        //retrieves data about status from database to set status imageview and textview
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();
        rootRef.child(firebaseAuth.getUid()).child("Shared Events").child(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String declineReason = dataSnapshot.child("Decline Reason").getValue(String.class);
                setStatusInfo(status, declineReason);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        titleTextView.setText(title);
        dateTextView.setText(date);
        timeTextView.setText(time);
        placeTextView.setText(place);
        setDescrTextView(descr);
    }


    private void setStatusInfo(String status, String declineReason){
        switch (status) {
            case Constants.WAITING:
                statusTextView.setText("Diese Einsendung wird noch bearbeitet");
                statusImage.setImageResource(R.drawable.ic_status_waiting);
                break;
            case Constants.ACCEPTED:
                statusTextView.setText("Diese Einsendung wurde veröffentlicht! Du findest sie in deinem Kalender!");
                statusImage.setImageResource(R.drawable.ic_status_accepted);
                break;
            case Constants.DECLINED:
                statusTextView.setText("Diese Einsendung wurde abgelehnt.\nGrund: " + declineReason);
                statusImage.setImageResource(R.drawable.ic_status_declined);
                break;
        }
    }


    private void setDescrTextView(String descr){
        if(!descr.isEmpty()){
            descrTextView.setText(descr);
        } else {
            descrTextView.setText("keine weiteren Details");
            descrTextView.setTypeface(null, Typeface.ITALIC);
        }
    }


}
