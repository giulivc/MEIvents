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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


//Fragment for Tab 1
//shows all published events in chosen period of time
//search function to filter through all events by title

public class AllEventsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {

    ArrayList<Event> acceptedEventsList;
    ListView acceptedEventsListView;
    PublishedEventsAdapter adapter;

    Spinner periodSpinner;
    ArrayAdapter<CharSequence> spinnerAdapter;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;

    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_events_tab, container, false);

        //search function
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();

        searchView = view.findViewById(R.id.all_events_searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchForEvent(newText);
                return true;
            }
        });

        //initializes listview and adapter
        acceptedEventsList = new ArrayList<>();

        acceptedEventsListView = view.findViewById(R.id.published_events_listView);
        acceptedEventsListView.setOnItemClickListener(this);
        acceptedEventsListView.setOnItemLongClickListener(this);

        adapter = new PublishedEventsAdapter(getContext(), acceptedEventsList);
        acceptedEventsListView.setAdapter(adapter);


        //spinner to choose period of time
        periodSpinner = view.findViewById(R.id.period_spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.period, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(spinnerAdapter);

        periodSpinner.setSelection(Constants.POSITION_NEXT_7_DAYS);
        periodSpinner.setOnItemSelectedListener(this);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        acceptedEventsListView = getView().findViewById(R.id.published_events_listView);
    }



    //BEARBEITEN!!!!
    private void searchForEvent(final String queryText){
        periodSpinner.setSelection(Constants.POSITION_ALL);
        rootRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                final DatabaseReference sharedEventsRef = rootRef.child(userProfile.getId()).child("Shared Events");
                Query query = sharedEventsRef.orderByChild("status").equalTo(Constants.ACCEPTED);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final Event acceptedEvent = snapshot.getValue(Event.class);
                                Query searchQuery = rootRef.child(acceptedEvent.getUserId()).child("Shared Events").orderByChild("title").startAt(queryText).endAt(queryText + "\uf8ff");
                                searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        acceptedEventsList.clear();
                                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                            Event searchedEvent  = snapshot.getValue(Event.class);
                                            acceptedEventsList.add(searchedEvent);
                                            adapter.sortByDate();
                                            adapter.notifyDataSetChanged();

                                        }
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

    //retrieves datasnapshot from firebase database with all accepted events and saves them in arraylist
    private void getAllAcceptedEvents() {
        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                final DatabaseReference sharedEventsRef = rootRef.child(userProfile.getId()).child("Shared Events");
                Query query = sharedEventsRef.orderByChild("status").equalTo(Constants.ACCEPTED);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Event acceptedEvent = snapshot.getValue(Event.class);
                                acceptedEventsList.add(acceptedEvent);
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
    }

    //defines what events have to be shown depending on selected period
    //searches for all accepted events and iterates over them only adding those to list who are within chosen period
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        acceptedEventsList.clear();

        rootRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                DatabaseReference sharedEventsRef = rootRef.child(userProfile.getId()).child("Shared Events");
                Query query = sharedEventsRef.orderByChild("status").equalTo(Constants.ACCEPTED);

                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                Event acceptedEvent = snapshot.getValue(Event.class);
                                Date date = acceptedEvent.getFormattedDate();

                                switch (position){

                                    case Constants.POSITION_TODAY:
                                        if(isToday(date)){
                                            acceptedEventsList.add(acceptedEvent);
                                            adapter.sortByDate();
                                            adapter.notifyDataSetChanged();
                                        }
                                        break;

                                    case Constants.POSITION_NEXT_7_DAYS:
                                        if(isInNext7Days(date)){
                                            acceptedEventsList.add(acceptedEvent);
                                            adapter.sortByDate();
                                            adapter.notifyDataSetChanged();
                                        }
                                        break;

                                    case Constants.POSITION_NEXT_30_DAYS:
                                        if(isInNext30Days(date)){
                                            acceptedEventsList.add(acceptedEvent);
                                            adapter.sortByDate();
                                            adapter.notifyDataSetChanged();
                                        }
                                        break;

                                    case Constants.POSITION_NEXT_3_MONTHS:
                                        if(isInNext90Days(date)){
                                            acceptedEventsList.add(acceptedEvent);
                                            adapter.sortByDate();
                                            adapter.notifyDataSetChanged();
                                        }
                                        break;

                                    case Constants.POSITION_ALL:
                                        acceptedEventsList.add(acceptedEvent);
                                        adapter.sortByDate();
                                        adapter.notifyDataSetChanged();
                                        break;
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    //opens PublishedEventInfoActivity with details to clicked event
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event publishedEvent = acceptedEventsList.get(position);

        Intent intent = new Intent(getActivity(), PublishedEventInfoActivity.class);
        intent.putExtra("USERID", publishedEvent.getUserId());
        intent.putExtra("EVENTID", publishedEvent.getEventId());
        intent.putExtra("TITLE", publishedEvent.getTitle());
        intent.putExtra("DATE", publishedEvent.getDate());
        intent.putExtra("TIME", publishedEvent.getTime());
        intent.putExtra("PLACE", publishedEvent.getPlace());
        intent.putExtra("ADMISSION", publishedEvent.getAdmission());
        intent.putExtra("DESCR", publishedEvent.getDescription());
        intent.putExtra("STATUS", publishedEvent.getStatus());

        startActivity(intent);
    }

    //adds event to TaggedEventsFragment and to database under "Tagged Events" on long click
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        Event taggedEvent = acceptedEventsList.get(position);

        rootRef.child(firebaseAuth.getUid()).child("Tagged Events").child(taggedEvent.getEventId()).setValue(taggedEvent);
        Toast.makeText(getContext(), "Du hast dir \"" + taggedEvent.getTitle() + "\" gemerkt", Toast.LENGTH_SHORT).show();
        return true;
    }



    //methods checking if event date is in certain period

    private boolean isToday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date today = calendar.getTime();
        return !date.after(today) && !date.before(today);
    }


    private boolean isInNext7Days(Date date){
        Calendar dayBeforeToday = Calendar.getInstance();
        dayBeforeToday.add(Calendar.DAY_OF_MONTH, -1);

        Date dateBeforeCurrentDate = dayBeforeToday.getTime();

        Calendar dateIn7Days = Calendar.getInstance();
        dateIn7Days.add(Calendar.DAY_OF_MONTH, 7);

        Date currentDateIn7Days = dateIn7Days.getTime();

        return date.after(dateBeforeCurrentDate) && date.before(currentDateIn7Days);
    }


    private boolean isInNext30Days(Date date){
        Calendar dayBeforeToday = Calendar.getInstance();
        dayBeforeToday.add(Calendar.DAY_OF_MONTH, -1);

        Date dateBeforeCurrentDate = dayBeforeToday.getTime();

        Calendar dateIn30Days = Calendar.getInstance();
        dateIn30Days.add(Calendar.DAY_OF_MONTH, 30);

        Date currentDateIn30Days = dateIn30Days.getTime();

        return date.after(dateBeforeCurrentDate) && date.before(currentDateIn30Days);
    }


    private boolean isInNext90Days(Date date){
        Calendar dayBeforeToday = Calendar.getInstance();
        dayBeforeToday.add(Calendar.DAY_OF_MONTH, -1);

        Date dateBeforeCurrentDate = dayBeforeToday.getTime();

        Calendar dateIn90Days = Calendar.getInstance();
        dateIn90Days.add(Calendar.DAY_OF_MONTH, 90);

        Date currentDateIn3Months = dateIn90Days.getTime();

        return date.after(dateBeforeCurrentDate) && date.before(currentDateIn3Months);
    }

}
