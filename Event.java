package com.example.meivents;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {

    String userId, eventId, title, date, time, place, admission, description, status;

    public Event(){

    }

    public Event (String userId, String eventId, String title, String date, String time, String place, String admission, String description, String status){
        this.userId = userId;
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.place = place;
        this.admission = admission;
        this.description = description;
        this.status = status;

    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public String getAdmission() {
        return admission;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }



    //sets image referring to status in SharedEventsFragment
    public void setStatusImage(final ImageView statusImageView){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference statusRef = firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Shared Events").child(eventId).child("status");
        statusRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                switch (status){
                    case Constants.WAITING:
                        statusImageView.setImageResource(R.drawable.ic_status_waiting);
                        break;
                    case Constants.ACCEPTED:
                        statusImageView.setImageResource(R.drawable.ic_status_accepted);
                        break;
                    case Constants.DECLINED:
                        statusImageView.setImageResource(R.drawable.ic_status_declined);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Date getFormattedDate(){
        try {
            DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN);
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
