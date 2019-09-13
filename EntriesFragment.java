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

//Fragment only usable for student council members
//showing all events that has been sent in by normal users that haven't been accepted or declined yet

public class EntriesFragment extends Fragment implements AdapterView.OnItemClickListener {


    ArrayList<Event> entriesList;
    ListView entriesListView;
    EntriesEventsAdapter adapter;

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

        View view = inflater.inflate(R.layout.fragment_entries, container, false);

        //sets title in actionbar
        getActivity().setTitle("Einsendungen");

        //initializes listview and adapter
        entriesList = new ArrayList<>();
        entriesListView = view.findViewById(R.id.entriesListView);
        entriesListView.setOnItemClickListener(this);

        adapter = new EntriesEventsAdapter(getContext(), entriesList);
        entriesListView.setAdapter(adapter);

        //searches for every user who isn't signed in as student council member
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();

        Query noStudCouncilQuery = rootRef.orderByChild("Student Council").equalTo(false);
        noStudCouncilQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserProfile userProfile = snapshot.getValue(UserProfile.class);
                        DatabaseReference ref = rootRef.child(userProfile.getId()).child("Shared Events");
                        ref.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Event newEntry = dataSnapshot.getValue(Event.class);
                                if (newEntry != null) {
                                    if(newEntry.getStatus().equals(Constants.WAITING)){
                                        entriesList.add(newEntry);
                                        adapter.notifyDataSetChanged();
                                    }
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        entriesListView = getView().findViewById(R.id.entriesListView);
    }

    //opens HandleEntryActivity when clicking item to accept or decline entry
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event entriesEvent = entriesList.get(position);

        Intent intent = new Intent(getActivity(), HandleEntryActivity.class);
        intent.putExtra("USERID", entriesEvent.getUserId());
        intent.putExtra("EVENTID", entriesEvent.getEventId());
        intent.putExtra("TITLE", entriesEvent.getTitle());
        intent.putExtra("DATE", entriesEvent.getDate());
        intent.putExtra("TIME", entriesEvent.getTime());
        intent.putExtra("PLACE", entriesEvent.getPlace());
        intent.putExtra("DESCR", entriesEvent.getDescription());

        startActivity(intent);
    }
}