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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */

public class CurrentTabFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<PublishedEventListItem> currentEventsList;
    ListView eventListView;

    PublishedEventListItemAdapter adapter;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference rootRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_tab, container, false);

        currentEventsList = new ArrayList<>();
        eventListView = view.findViewById(R.id.published_events_listView);
        eventListView.setOnItemClickListener(this);
        adapter = new PublishedEventListItemAdapter(getContext(), currentEventsList);
        eventListView.setAdapter(adapter);


        firebaseDatabase = FirebaseDatabase.getInstance();
        rootRef = firebaseDatabase.getReference();

        rootRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                final DatabaseReference sharedEventsRef = rootRef.child(userProfile.getId()).child("Shared Events");
                Query query = sharedEventsRef.orderByChild("status").equalTo(Constants.ACCEPTED);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Event event = snapshot.getValue(Event.class);
                                String dateString = event.getDate();
                                Date date = formatToDate(dateString);
                                if(isInNextSevenDays(date)){
                                    PublishedEventListItem currentPublishedEventListItem = event.getPublishedEventListItemFromEvent(event);
                                    currentEventsList.add(currentPublishedEventListItem);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        eventListView = getView().findViewById(R.id.published_events_listView);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PublishedEventListItem publishedEventListItem = currentEventsList.get(position);
        DatabaseReference eventRef = rootRef.child(publishedEventListItem.getUserId()).child("Shared Events");
        Query query = eventRef.orderByChild("eventId").equalTo(publishedEventListItem.getEventId());
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


    private Date formatToDate(String date){
        try {
            DateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN);
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isInNextSevenDays(Date date){
        long today = System.currentTimeMillis();
        long dateInMs = date.getTime();

        return dateInMs >= today && dateInMs <= (today + 604800000);
    }
}
