package com.example.meivents;


public class Event {

    String eventId, title, date, time, place, admission, description, status, userId;
    Boolean reminderOn;

    Boolean done;

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






    public SharedEventListItem getSharedEventListItemFromEvent(Event event){
        String id = event.getEventId();
        String title = event.getTitle();
        String date = event.getDate();
        String status = event.getStatus();
        return new SharedEventListItem(id, title, date, status);
    }


    public PublishedEventListItem getPublishedEventListItemFromEvent(Event event){
        String eventId = event.getEventId();
        String userId = event.getUserId();
        String title = event.getTitle();
        String date = event.getDate();
        return new PublishedEventListItem(eventId, userId, title, date);
    }

    public EntriesEventListItem getEntriesEventListItemFromEvent(Event event){
        String eventId = event.getEventId();
        String userId = event.getUserId();
        String title = event.getTitle();
        String date = event.getDate();
        return new EntriesEventListItem(userId, eventId, title, date);
    }


}
