package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SharedEventListItemAdapter extends ArrayAdapter<SharedEventListItem> {

    private ArrayList<SharedEventListItem> sharedEventList;
    private Context context;

    public SharedEventListItemAdapter(Context context, ArrayList<SharedEventListItem> sharedEventList) {
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

        SharedEventListItem sharedEventListItem = sharedEventList.get(position);

        if (sharedEventListItem != null) {
            TextView sharedEventItemTitle = v.findViewById(R.id.title_info_textView);
            TextView sharedEventItemDate = v.findViewById(R.id.date_textView);

            sharedEventItemTitle.setText(sharedEventListItem.getTitle());
            sharedEventItemDate.setText(sharedEventListItem.getDate());
            sharedEventListItem.setImage(v);
        }
        return v;
    }
}
