package com.example.meivents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


//Fragment showing all published events tagged by user in listview

public class TaggedEventsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ArrayList<Event> taggedEventsList;
    ListView taggedEventsListView;
    PublishedEventsAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tagged_events_tab, container, false);

        //initializes listview and adapter
        taggedEventsList = new ArrayList<>();

        taggedEventsListView = view.findViewById(R.id.tagged_events_listView);
        adapter = new PublishedEventsAdapter(getContext(), taggedEventsList);
        taggedEventsListView.setAdapter(adapter);
        taggedEventsListView.setOnItemClickListener(this);
        taggedEventsListView.setOnItemLongClickListener(this);


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();

        final DatabaseReference taggedEventsRef = rootRef.child(firebaseAuth.getUid()).child("Tagged Events");
        taggedEventsRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event taggedEvent = dataSnapshot.getValue(Event.class);
                if (taggedEvent != null) {
                    taggedEventsList.add(taggedEvent);
                    adapter.sortByDate();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    //opens PublishedEventInfoActivity with details to clicked event
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event taggedEvent = taggedEventsList.get(position);

        Intent intent = new Intent(getActivity(), PublishedEventInfoActivity.class);
        intent.putExtra("EVENTID", taggedEvent.getEventId());
        intent.putExtra("USERID", taggedEvent.getUserId());
        intent.putExtra("TITLE", taggedEvent.getTitle());
        intent.putExtra("DATE", taggedEvent.getDate());
        intent.putExtra("TIME", taggedEvent.getTime());
        intent.putExtra("PLACE", taggedEvent.getPlace());
        intent.putExtra("ADMISSION", taggedEvent.getAdmission());
        intent.putExtra("DESCR", taggedEvent.getDescription());
        intent.putExtra("STATUS", taggedEvent.getStatus());

        startActivity(intent);
    }


    //removes event from TaggedEventsFragment and from database under "Tagged Events" on long click
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
        Event taggedEvent = taggedEventsList.get(position);

        rootRef.child(firebaseAuth.getUid()).child("Tagged Events").child(taggedEvent.getEventId()).removeValue();
        taggedEventsList.remove(taggedEvent);
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Du hast \"" + taggedEvent.getTitle() + "\" aus \"Gemerkt\" entfernt", Toast.LENGTH_SHORT).show();
        return true;
    }

}
