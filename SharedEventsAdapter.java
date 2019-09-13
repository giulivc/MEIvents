package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//adapter class for displaying own shared events in SharedEventsFragment
//adds image of processing status to each list item

public class SharedEventsAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> sharedEventList;
    private Context context;


    public SharedEventsAdapter(Context context, ArrayList<Event> sharedEventList) {
        super(context, R.layout.shared_event_list_item, sharedEventList);

        this.sharedEventList = sharedEventList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.shared_event_list_item, null);
        }

        Event sharedEvent = sharedEventList.get(position);

        if (sharedEvent != null) {
            TextView sharedEventItemTitle = v.findViewById(R.id.title_published_list_textView);
            TextView sharedEventItemDate = v.findViewById(R.id.date_published_list_textView);
            ImageView sharedEventItemImage = v.findViewById(R.id.status_imageView);

            sharedEventItemTitle.setText(sharedEvent.getTitle());
            sharedEventItemDate.setText(sharedEvent.getDate());
            sharedEvent.setStatusImage(sharedEventItemImage);
        }
        return v;
    }
}
