package com.example.meivents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class SharedEventsFragment extends Fragment {

    ArrayList<SharedEventItem> sharedEventList;
    ListView sharedEventsListView;

    SharedEventItemAdapter adapter;
    SharedEventItem sharedEventItem1;
    SharedEventItem sharedEventItem2;
    SharedEventItem sharedEventItem3;
    SharedEventItem sharedEventItem4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_events, container, false);

        sharedEventList = new ArrayList<>();
        sharedEventItem1 = new SharedEventItem("Party", "05.06.00", Constants.WAITING);
        sharedEventList.add(sharedEventItem1);
        sharedEventItem2 = new SharedEventItem("Party 2 ", "26.01.00", Constants.WAITING);
        sharedEventList.add(sharedEventItem2);
        sharedEventItem3 = new SharedEventItem("Bruder muss los", "23.08.19", Constants.DECLINED);
        sharedEventList.add(sharedEventItem3);
        sharedEventItem4 = new SharedEventItem("Süßi Raphael Wimmer Lieblingslesung", "21.09.219", Constants.ACCEPTED);
        sharedEventList.add(sharedEventItem4);



        sharedEventsListView = view.findViewById(R.id.sharedEventsListView);
        adapter = new SharedEventItemAdapter(getContext(), sharedEventList);
        sharedEventsListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedEventsListView = getView().findViewById(R.id.sharedEventsListView);
    }
}