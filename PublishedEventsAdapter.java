package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//adapter class for displaying published events in AllEventsFragment and TaggedEventsFragment

public class PublishedEventsAdapter extends ArrayAdapter<Event> {

    private Context context;
    private ArrayList<Event> publishedEventsList;


    public PublishedEventsAdapter(Context context, ArrayList<Event> publishedEventsList) {
        super(context, R.layout.published_event_list_item, publishedEventsList);

        this.context = context;
        this.publishedEventsList = publishedEventsList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.published_event_list_item, null);
        }

        Event publishedEvent = publishedEventsList.get(position);

        if (publishedEvent != null) {
            TextView eventTitle = v.findViewById(R.id.title_published_list_textView);
            TextView eventDate = v.findViewById(R.id.date_published_list_textView);

            eventTitle.setText(publishedEvent.getTitle());
            eventDate.setText(publishedEvent.getDate());
        }
        return v;
    }

    //sorts listview by date of event
    public void sortByDate(){
        Collections.sort(publishedEventsList, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                if (event1.getFormattedDate() == null || event2.getFormattedDate() == null) {
                    return 0;
                }
                return event1.getFormattedDate().compareTo(event2.getFormattedDate());
            }
        });
    }

}
