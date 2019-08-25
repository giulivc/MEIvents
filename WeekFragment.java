package com.example.meivents;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */

public class WeekFragment extends Fragment {

    ArrayList<EventItem> eventList;
    ListView eventListView;

    EventItemAdapter adapter;
    EventItem eventTest1;
    EventItem eventTest2;
    EventItem eventTest3;
    EventItem eventTest4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_week, container, false);

        eventList = new ArrayList<>();
        eventTest1 = new EventItem("Party", "05.06.00");
        eventList.add(eventTest1);
        eventTest2 = new EventItem("Party 2 ", "26.01.00");
        eventList.add(eventTest2);
        eventTest3 = new EventItem("Bruder muss los", "23.08.19");
        eventList.add(eventTest3);
        eventTest4 = new EventItem("Süßi Raphael Wimmer Lieblingslesung", "21.09.219");
        eventList.add(eventTest4);



        eventListView = view.findViewById(R.id.event_listview);
        adapter = new EventItemAdapter(getContext(), eventList);
        eventListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        eventListView = getView().findViewById(R.id.event_listview);
    }

}
