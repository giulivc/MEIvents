package com.example.meivents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

}
