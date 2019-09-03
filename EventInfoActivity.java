package com.example.meivents;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EventInfoActivity extends AppCompatActivity implements Switch.OnCheckedChangeListener {

    TextView titleTextView, dateTextView, timeTextView, placeTextView, descrTextView;
    Switch reminderSwitch;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        titleTextView = findViewById(R.id.title_info_textView);
        dateTextView = findViewById(R.id.date_info_textView);
        timeTextView = findViewById(R.id.time_info_textView);
        placeTextView = findViewById(R.id.place_info_textView);
        descrTextView = findViewById(R.id.descr_info_textView);

        reminderSwitch = findViewById(R.id.reminder_switch);
        reminderSwitch.setOnCheckedChangeListener(this);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("TITLE");
        String date = extras.getString("DATE");
        String time = extras.getString("TIME");
        String place = extras.getString("PLACE");
        String descr = extras.getString("DESCR");
        id = extras.getString("ID");

        titleTextView.setText(title);
        dateTextView.setText(date);
        timeTextView.setText(time);
        placeTextView.setText(place);
        if(!descr.isEmpty()){
            descrTextView.setText(descr);
        } else {
            descrTextView.setText("keine weiteren Details");
            descrTextView.setTypeface(null, Typeface.ITALIC);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(reminderSwitch.isChecked()){
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Shared Events").child(id).child("reminderOn").setValue(true);
        } else {
            firebaseDatabase.getReference().child(firebaseAuth.getUid()).child("Shared Events").child(id).child("reminderOn").setValue(false);
        }
    }
}
