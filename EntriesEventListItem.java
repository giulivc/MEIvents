package com.example.meivents;

import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EntriesEventListItem {

    private String userId, eventId, title, date;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    public EntriesEventListItem(String userId, String eventId, String title, String date){
        this.userId = userId;
        this.eventId = eventId;
        this.title = title;
        this.date = date;
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





}
