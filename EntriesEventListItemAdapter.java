package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EntriesEventListItemAdapter extends ArrayAdapter<EntriesEventListItem> {

    private ArrayList<EntriesEventListItem> entriesList;
    private Context context;

    public EntriesEventListItemAdapter(Context context, ArrayList<EntriesEventListItem> entriesList) {
        super(context, R.layout.entries_event_list_item, entriesList);

        this.entriesList = entriesList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.entries_event_list_item, null);
        }

        EntriesEventListItem entriesEventListItem = entriesList.get(position);

        if (entriesEventListItem != null) {
            TextView entriesEventItemTitle = v.findViewById(R.id.title_entries_list_textView);
            TextView entriesEventItemDate = v.findViewById(R.id.date_entries_list_textView);

            entriesEventItemTitle.setText(entriesEventListItem.getTitle());
            entriesEventItemDate.setText(entriesEventListItem.getDate());
        }

        return v;
    }

}
