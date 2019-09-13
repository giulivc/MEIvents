package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//adapter class for displaying entries in EntriesFragment
//adds non-functional unread dot to every list item (design attribute)

public class EntriesEventsAdapter extends ArrayAdapter<Event> {


    private Context context;
    private ArrayList<Event> entriesList;


    public EntriesEventsAdapter(Context context, ArrayList<Event> entriesList) {
        super(context, R.layout.entries_event_list_item, entriesList);

        this.context = context;
        this.entriesList = entriesList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.entries_event_list_item, null);
        }

       Event entriesEvent = entriesList.get(position);

        if (entriesEvent != null) {
            TextView entriesEventItemTitle = v.findViewById(R.id.title_entries_list_textView);
            TextView entriesEventItemDate = v.findViewById(R.id.date_entries_list_textView);

            entriesEventItemTitle.setText(entriesEvent.getTitle());
            entriesEventItemDate.setText(entriesEvent.getDate());
        }

        return v;
    }

}
