package com.example.meivents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//Fragment with two tabs for displaying all published events and user-tagged events

public class EventCalendarFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    TabLayout tabLayout;

    ViewPager viewPager;
    PageAdapter pageAdapter;

    FloatingActionButton noStudCouncilFloatingActionButton, studCouncilFloatingActionButton;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;
    DatabaseReference studCouncilRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_calender, container, false);

        //sets title in actionbar
        getActivity().setTitle("Event Kalender");

        //functionalizes tablayout with AllEventsFragment as tab 1 and TaggedEventsFragment as tab 2
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //floatingActionButton that opens ShareEventActivity for normal users
        noStudCouncilFloatingActionButton = view.findViewById(R.id.noFachschaft_floatingActionButton);
        noStudCouncilFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getContext(), ShareEventActivity.class);
                startActivity(i);
            }
        });

        //floatingActionButton that opens PublishEventActivity for student council members
        //first hidden, shown when user cahnges to student council member
        studCouncilFloatingActionButton = view.findViewById(R.id.fachschaft_floatingActionButton);
        studCouncilFloatingActionButton.hide();
        studCouncilFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getContext(), PublishEventActivity.class);
                startActivity(i);
            }
        });

        //listens for user changing to student council member
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();

        studCouncilRef = rootRef.child(firebaseAuth.getUid()).child("Student Council");
        studCouncilRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isStudCouncil = dataSnapshot.getValue(Boolean.class);
                if(isStudCouncil) {
                    noStudCouncilFloatingActionButton.hide();
                    studCouncilFloatingActionButton.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
}


