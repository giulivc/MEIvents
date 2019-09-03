package com.example.meivents;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EventListItem {

    private String title;
    //private GregorianCalendar date;
    private String date;

    public EventListItem(String title, String date) {
        this.title = title;
        //this.date = getDateFromString(date);
        this.date = date;
    }

    /*private GregorianCalendar getDateFromString(String date) {
        GregorianCalendar cal = new GregorianCalendar();

        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
            cal.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }*/

    /*public String getFormattedDate() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);

        return df.format(date.getTime());
    }*/

    public String getDate(){
        return date;
    }

    public String getTitle(){return title;}


}

