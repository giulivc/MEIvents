package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PublishedEventListItemAdapter extends ArrayAdapter<PublishedEventListItem> {

    private ArrayList<PublishedEventListItem> eventList;
    private Context context;

    public PublishedEventListItemAdapter(Context context, ArrayList<PublishedEventListItem> eventList) {
        super(context, R.layout.published_event_list_item, eventList);

        this.eventList = eventList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.published_event_list_item, null);
        }

        PublishedEventListItem publishedEventListItem = eventList.get(position);

        if (publishedEventListItem != null) {
            TextView eventItemTitle = v.findViewById(R.id.title_info_textView);
            TextView eventItemDate = v.findViewById(R.id.date_textView);

            eventItemTitle.setText(publishedEventListItem.getTitle());
            eventItemDate.setText(publishedEventListItem.getDate());
        }
        return v;
    }
}
