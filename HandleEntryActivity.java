package com.example.meivents;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HandleEntryActivity extends AppCompatActivity implements View.OnClickListener {

    TextView titleTextView, dateTextView, timeTextView, placeTextView, descrTextView;
    ImageView declineImageView, acceptImageView;

    TextView dialogDeclinedTextView;
    Button okayDeclineButton, quitDeclineButton;

    String title, eventId, userId;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_entry);

        titleTextView = findViewById(R.id.title_entry_textView);
        dateTextView = findViewById(R.id.date_entry_textView);
        timeTextView = findViewById(R.id.time_entry_textView);
        placeTextView = findViewById(R.id.place_entry_textView);
        descrTextView = findViewById(R.id.descr_entry_textView);

        declineImageView = findViewById(R.id.decline_imageView);
        declineImageView.setOnClickListener(this);
        acceptImageView = findViewById(R.id.accept_imageView);
        acceptImageView.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        title = extras.getString("TITLE");
        String date = extras.getString("DATE");
        String time = extras.getString("TIME");
        String place = extras.getString("PLACE");
        String descr = extras.getString("DESCR");
        eventId = extras.getString("EVENTID");
        userId = extras.getString("USERID");

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
    public void onClick(View v) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference rootRef = firebaseDatabase.getReference();
        final DatabaseReference statusRef = rootRef.child(userId).child("Shared Events").child(eventId).child("status");

        switch (v.getId()){
            case R.id.decline_imageView:
                final Dialog declineDialog = new Dialog(this);
                declineDialog.setContentView(R.layout.dialog_declined);
                dialogDeclinedTextView = declineDialog.findViewById(R.id.dialog_decline_textView);
                dialogDeclinedTextView.setText("Bei Klicken auf \"Okay!\" wird das Event \""+ title +"\" abgelehnt!");
                okayDeclineButton = declineDialog.findViewById(R.id.okay_decline_button);
                okayDeclineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusRef.setValue(Constants.DECLINED);
                        declineDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Event \""+ title +"\" wurde abgelehnt!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                quitDeclineButton = declineDialog.findViewById(R.id.quit_decline_button);
                quitDeclineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        declineDialog.dismiss();
                    }
                });

                declineDialog.show();
                break;
            case R.id.accept_imageView:
                final Dialog acceptDialog = new Dialog(this);
                acceptDialog.setContentView(R.layout.dialog_accepted);
                dialogDeclinedTextView = acceptDialog.findViewById(R.id.dialog_accept_textView);
                dialogDeclinedTextView.setText("Bei Klicken auf \"Okay!\" wird das Event \""+ title +"\" veröffentlicht!");
                okayDeclineButton = acceptDialog.findViewById(R.id.okay_accept_button);
                okayDeclineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        statusRef.setValue(Constants.ACCEPTED);
                        acceptDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Event \""+ title +"\" wurde veröffentlicht!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                quitDeclineButton = acceptDialog.findViewById(R.id.quit_accept_button);
                quitDeclineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptDialog.dismiss();
                    }
                });

                acceptDialog.show();
                break;
        }

    }
}
