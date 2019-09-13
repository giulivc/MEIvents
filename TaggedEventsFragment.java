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
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

public class TaggedEventsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;

    ArrayList<Event> taggedEventsList;
    ListView taggedEventsListView;
    PublishedEventsAdapter adapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tagged_events_tab, container, false);


        taggedEventsList = new ArrayList<>();
        taggedEventsListView = view.findViewById(R.id.tagged_events_listView);
        adapter = new PublishedEventsAdapter(getContext(), taggedEventsList);
        taggedEventsListView.setAdapter(adapter);
        taggedEventsListView.setOnItemClickListener(this);
        taggedEventsListView.setOnItemLongClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();


        new Thread() {
            public void run(){
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
            }
        }.start();

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event taggedEvent = taggedEventsList.get(position);
        DatabaseReference eventRef = rootRef.child(firebaseAuth.getUid()).child("Tagged Events");
        Query query = eventRef.orderByChild("eventId").equalTo(taggedEvent.getEventId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event event = snapshot.getValue(Event.class);

                        Intent intent = new Intent(getActivity(), PublishedEventInfoActivity.class);
                        intent.putExtra("EVENTID", event.getEventId());
                        intent.putExtra("USERID", event.getUserId());
                        intent.putExtra("TITLE", event.getTitle());
                        intent.putExtra("DATE", event.getDate());
                        intent.putExtra("TIME", event.getTime());
                        intent.putExtra("PLACE", event.getPlace());
                        intent.putExtra("ADMISSION", event.getAdmission());
                        intent.putExtra("DESCR", event.getDescription());
                        intent.putExtra("STATUS", event.getStatus());

                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
        Event taggedEvent = taggedEventsList.get(position);
        DatabaseReference eventRef = rootRef.child(taggedEvent.getUserId()).child("Shared Events");
        Query query = eventRef.orderByChild("eventId").equalTo(taggedEvent.getEventId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event taggedEvent = snapshot.getValue(Event.class);
                        rootRef.child(firebaseAuth.getUid()).child("Tagged Events").child(taggedEvent.getEventId()).removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }

}
