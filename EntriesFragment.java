package com.example.meivents;

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
import com.google.firebase.auth.FirebaseUser;
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

public class EntriesFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<EventListItem> entriesList;
    ListView entriesListView;

    EventListItemAdapter adapter;

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


        entriesListView = view.findViewById(R.id.entriesListView);
        entriesListView.setOnItemClickListener(this);
        adapter = new EventListItemAdapter(getContext(), entriesList);
        entriesListView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        rootRef = firebaseDatabase.getReference();


        Query noFachschaftQuery = rootRef.orderByChild("Fachschaftsmitglied").equalTo(false);
        noFachschaftQuery.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
                    DatabaseReference ref = rootRef.child(userProfile.getId()).child("Shared Events");
                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Event event = dataSnapshot.getValue(Event.class);
                            if(event != null){
                                EventListItem newEntry = event.getEventListItemFromEvent(event);
                                entriesList.add(newEntry);
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
        entriesListView = getView().findViewById(R.id.event_listview);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}