package com.example.meivents;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Event {

    String id, title, date, time, place, admission, description, status;
    Boolean reminderOn;

    public Event(){

    }

    public Event (String id,String title, String date, String time, String place, String admission, String description, String status, Boolean reminderOn){
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.place = place;
        this.admission = admission;
        this.description = description;
        this.status = status;
        this.reminderOn = reminderOn;


    }

    public String getId() {
        return id;
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

    public String getStatus() {
        return status;
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

    public Boolean getReminderOn(){
        return reminderOn;
    }


    public SharedEventItem getSharedEventListItemFromEvent(Event event){
        String title = event.getTitle();
        String date = event.getDate();
        String id = event.getId();
        return new SharedEventItem(id, title, date, Constants.WAITING);
    }


    public EventListItem getEventListItemFromEvent(Event event){
        String title = event.getTitle();
        String date = event.getDate();
        return new EventListItem(title, date);
    }
}
