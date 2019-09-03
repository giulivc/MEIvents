package com.example.meivents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */

public class WeekFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<EventListItem> eventList;
    ListView eventListView;

    EventListItemAdapter adapter;
    EventListItem eventTest1;
    EventListItem eventTest2;
    EventListItem eventTest3;
    EventListItem eventTest4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_week, container, false);

        eventList = new ArrayList<>();
        eventTest1 = new EventListItem("Party", "05.06.00");
        eventList.add(eventTest1);
        eventTest2 = new EventListItem("Party 2 ", "26.01.00");
        eventList.add(eventTest2);
        eventTest3 = new EventListItem("Bruder muss los", "23.08.19");
        eventList.add(eventTest3);
        eventTest4 = new EventListItem("Süßi Raphael Wimmer Lieblingslesung", "21.09.219");
        eventList.add(eventTest4);



        eventListView = view.findViewById(R.id.event_listview);
        adapter = new EventListItemAdapter(getContext(), eventList);
        eventListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        eventListView = getView().findViewById(R.id.event_listview);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), Event.class);
        startActivity(i);

    }
}
