package com.example.meivents;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PublishedEventListItem {

    String eventId, title, date, userId;

    public PublishedEventListItem(String eventId, String userId, String title, String date) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.date = date;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate(){
        return date;
    }

    public String getTitle(){return title;}


}

