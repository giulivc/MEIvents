package com.example.meivents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SharedEventItemAdapter extends ArrayAdapter<SharedEventItem> {

    private ArrayList<SharedEventItem> sharedEventList;
    private Context context;

    public SharedEventItemAdapter(Context context, ArrayList<SharedEventItem> sharedEventList) {
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

        SharedEventItem sharedEventItem = sharedEventList.get(position);

        if (sharedEventItem != null) {
            TextView sharedEventItemTitle = v.findViewById(R.id.titleTextView);
            TextView sharedEventItemDate = v.findViewById(R.id.dateTextView);

            sharedEventItemTitle.setText(sharedEventItem.getTitle());
            sharedEventItemDate.setText(sharedEventItem.getDate());
            sharedEventItem.setImage(v, sharedEventItem.getStatus());
        }
        return v;
    }
}
