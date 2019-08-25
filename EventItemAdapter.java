package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventItemAdapter extends ArrayAdapter<EventItem> {

    private ArrayList<EventItem> eventList;
    private Context context;

    public EventItemAdapter(Context context, ArrayList<EventItem> eventList) {
        super(context, R.layout.event_list_item, eventList);

        this.eventList = eventList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.event_list_item, null);
        }

        EventItem eventItem = eventList.get(position);

        if (eventItem != null) {
            TextView eventItemTitle = v.findViewById(R.id.titleTextView);
            TextView eventItemDate = v.findViewById(R.id.dateTextView);

            eventItemTitle.setText(eventItem.getTitle());
            eventItemDate.setText(eventItem.getDate());
        }
        return v;
    }
}
