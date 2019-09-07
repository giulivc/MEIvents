package com.example.meivents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */

public class EventCalenderFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener {

    TabLayout tabLayout;
    TabItem weekViewTab;
    TabItem monthViewTab;

    ViewPager viewPager;
    PageAdapter pageAdapter;

    FloatingActionButton floatingActionButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootRef;
    CalendarView calendarView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_calender, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        weekViewTab = view.findViewById(R.id.weekview_tab);
        monthViewTab = view.findViewById(R.id.monthview_tab);


        viewPager = view.findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(this);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);

        calendarView = view.findViewById(R.id.calendarView);



        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        Intent i  = new Intent(getContext(), ShareEventActivity.class);
        startActivity(i);
    }

    private void addEventToCalender() {
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
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Event event = snapshot.getValue(Event.class);


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

                @Override
                public void onChildChanged (@NonNull DataSnapshot dataSnapshot, @Nullable String s){

                }

                @Override
                public void onChildRemoved (@NonNull DataSnapshot dataSnapshot){

                }

                @Override
                public void onChildMoved (@NonNull DataSnapshot dataSnapshot, @Nullable String s){

                }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }
        });
    }
}

