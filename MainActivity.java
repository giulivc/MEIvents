package com.example.meivents;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



//implements fundamental navigation trough app with burger menu

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;
    DatabaseReference studCouncilRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventCalendarFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_event_calender);
        }

        navigationView.getMenu().findItem(R.id.nav_entries).setEnabled(false);

        //listens for user changing to student council member to disable menu item "Fachschafts-Login" and enable "Einsendungen"
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();

        studCouncilRef = rootRef.child(firebaseAuth.getUid()).child("Student Council");
        studCouncilRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isStudCouncil = dataSnapshot.getValue(Boolean.class);
                if(isStudCouncil) {
                    navigationView.getMenu().findItem(R.id.nav_login).setEnabled(false);
                    navigationView.getMenu().findItem(R.id.nav_entries).setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_event_calender:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventCalendarFragment()).commit();
                break;
            case R.id.nav_shared_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SharedEventsFragment()).commit();
                break;
            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentCouncilLoginFragment()).commit();
                break;
            case R.id.nav_entries:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EntriesFragment()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
