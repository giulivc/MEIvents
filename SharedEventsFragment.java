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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class SharedEventsFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<Event> sharedEventsList;
    ListView sharedEventsListView;
    SharedEventsAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    DatabaseReference rootRef;
    DatabaseReference sharedEventsRef;

    ChildEventListener childEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_events, container, false);

        getActivity().setTitle("Geteilte Events");

        sharedEventsList = new ArrayList<>();
        sharedEventsListView = view.findViewById(R.id.sharedEventsListView);
        sharedEventsListView.setOnItemClickListener(this);
        adapter = new SharedEventsAdapter(getContext(), sharedEventsList);
        sharedEventsListView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth  = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();
        sharedEventsRef = rootRef.child(firebaseAuth.getUid()).child("Shared Events");

        childEventListener = new ChildEventListener(){
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event sharedEvent = dataSnapshot.getValue(Event.class);
                if(sharedEvent != null){
                    sharedEventsList.add(sharedEvent);
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
        };

        sharedEventsRef.addChildEventListener(childEventListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedEventsListView = getView().findViewById(R.id.sharedEventsListView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event sharedEvent = sharedEventsList.get(position);
        sharedEventsRef = firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Shared Events");
        Query query = sharedEventsRef.orderByChild("eventId").equalTo(sharedEvent.getEventId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event event = snapshot.getValue(Event.class);

                        Intent intent = new Intent(getActivity(), SharedEventInfoActivity.class);
                        intent.putExtra("EVENTID", event.getEventId());
                        intent.putExtra("TITLE", event.getTitle());
                        intent.putExtra("DATE", event.getDate());
                        intent.putExtra("TIME", event.getTime());
                        intent.putExtra("PLACE", event.getPlace());
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
}