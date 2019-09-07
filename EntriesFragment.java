package com.example.meivents;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class EntriesFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<EntriesEventListItem> entriesList;
    ListView entriesListView;

    EntriesEventListItemAdapter adapter;

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

        entriesList = new ArrayList<>();
        Collections.reverse(entriesList);

        entriesListView = view.findViewById(R.id.entriesListView);
        entriesListView.setOnItemClickListener(this);

        adapter = new EntriesEventListItemAdapter(getContext(), entriesList);
        entriesListView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        rootRef = firebaseDatabase.getReference();

        Query noFachschaftQuery = rootRef.orderByChild("Fachschaftsmitglied").equalTo(false);
        noFachschaftQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserProfile userProfile = snapshot.getValue(UserProfile.class);
                        final DatabaseReference ref = rootRef.child(userProfile.getId()).child("Shared Events");
                        ref.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                final Event event = dataSnapshot.getValue(Event.class);
                                if (event != null) {
                                    if(event.getStatus().equals(Constants.WAITING)){
                                        final EntriesEventListItem newEntry = event.getEntriesEventListItemFromEvent(event);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EntriesEventListItem entriesEventListItem = entriesList.get(position);
        System.out.println(entriesEventListItem.getUserId());

        DatabaseReference eventRef = rootRef.child(entriesEventListItem.getUserId()).child("Shared Events");
        Query query = eventRef.orderByChild("eventId").equalTo(entriesEventListItem.getEventId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event event = snapshot.getValue(Event.class);

                        Intent intent = new Intent(getActivity(), HandleEntryActivity.class);
                        intent.putExtra("USERID", event.getUserId());
                        intent.putExtra("EVENTID", event.getEventId());
                        intent.putExtra("TITLE", event.getTitle());
                        intent.putExtra("DATE", event.getDate());
                        intent.putExtra("TIME", event.getTime());
                        intent.putExtra("PLACE", event.getPlace());
                        intent.putExtra("DESCR", event.getDescription());

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