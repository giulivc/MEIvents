package com.example.meivents;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SharedEventInfoActivity extends AppCompatActivity {

    TextView titleTextView, dateTextView, timeTextView, placeTextView, descrTextView, statusTextView;
    ImageView statusImage;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_event_info);

        titleTextView = findViewById(R.id.title_shared_textView);
        dateTextView = findViewById(R.id.date_shared_textView);
        timeTextView = findViewById(R.id.time_shared_textView);
        placeTextView = findViewById(R.id.place_shared_textView);
        descrTextView = findViewById(R.id.descr_shared_textView);
        statusTextView = findViewById(R.id.status_shared_textView);
        statusImage = findViewById(R.id.status_shared_imageView);


        Bundle extras = getIntent().getExtras();
        String title = extras.getString("TITLE");
        String date = extras.getString("DATE");
        String time = extras.getString("TIME");
        String place = extras.getString("PLACE");
        String descr = extras.getString("DESCR");
        String status = extras.getString("STATUS");

        titleTextView.setText(title);
        dateTextView.setText(date);
        timeTextView.setText(time);
        placeTextView.setText(place);
        setDescrTextView(descr);
        setStatusInfo(status);

    }

    private void setDescrTextView(String descr){
        if(!descr.isEmpty()){
            descrTextView.setText(descr);
        } else {
            descrTextView.setText("keine weiteren Details");
            descrTextView.setTypeface(null, Typeface.ITALIC);
        }
    }

    private void setStatusInfo(String status){
        switch (status) {
            case Constants.WAITING:
                statusTextView.setText("Diese Einsendung wird noch bearbeitet");
                statusImage.setImageResource(R.drawable.ic_status_waiting);
                break;
            case Constants.ACCEPTED:
                statusTextView.setText("Diese Einsendung wurde akzeptiert und veröffentlicht! Du findest sie in deinem Kalender!");
                statusImage.setImageResource(R.drawable.ic_status_accepted);
                break;
            case Constants.DECLINED:
                statusTextView.setText("Diese Einsendung wurde abgelehnt. Grund: ");
                statusImage.setImageResource(R.drawable.ic_status_declined);
                break;
        }
    }
}
