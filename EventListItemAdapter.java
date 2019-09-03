package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventListItemAdapter extends ArrayAdapter<EventListItem> {

    private ArrayList<EventListItem> eventList;
    private Context context;

    public EventListItemAdapter(Context context, ArrayList<EventListItem> eventList) {
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

        EventListItem eventListItem = eventList.get(position);

        if (eventListItem != null) {
            TextView eventItemTitle = v.findViewById(R.id.title_info_textView);
            TextView eventItemDate = v.findViewById(R.id.date_textView);

            eventItemTitle.setText(eventListItem.getTitle());
            eventItemDate.setText(eventListItem.getDate());
        }
        return v;
    }
}
