package com.example.meivents;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SharedEventListItem {

    private String eventId, title, date, status;

    ImageView statusImage;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    public SharedEventListItem(String eventId, String title, String date, String status){
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setImage(View v){
        statusImage = v.findViewById(R.id.status_imageView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference statusRef = firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Shared Events").child(eventId).child("status");
        statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                switch (status){
                    case Constants.WAITING:
                        statusImage.setImageResource(R.drawable.ic_status_waiting);
                        break;
                    case Constants.ACCEPTED:
                        statusImage.setImageResource(R.drawable.ic_status_accepted);
                        break;
                    case Constants.DECLINED:
                        statusImage.setImageResource(R.drawable.ic_status_declined);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
