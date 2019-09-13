package com.example.meivents;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Activity for student council members to accept or decline events sent in by users

public class HandleEntryActivity extends AppCompatActivity implements View.OnClickListener{

    TextView titleTextView, dateTextView, timeTextView, placeTextView, descrTextView;
    ImageView declineImageView, acceptImageView;

    String userId, eventId, title, date, time, place, descr;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;
    DatabaseReference statusRef;

    TextView dialogDeclinedTextView;
    Button okayDeclineButton, quitDeclineButton;

    TextView dialogAcceptedTextView;
    Button okayAcceptButton, quitAcceptButton;

    Spinner reasonSpinner;
    ArrayAdapter<CharSequence> adapter;
    Button reasonSendButton;


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
        userId = extras.getString("USERID");
        eventId = extras.getString("EVENTID");
        title = extras.getString("TITLE");
        date = extras.getString("DATE");
        time = extras.getString("TIME");
        place = extras.getString("PLACE");
        descr = extras.getString("DESCR");


        titleTextView.setText(title);
        dateTextView.setText(date);
        timeTextView.setText(time);
        placeTextView.setText(place);
        setDescrTextView(descr);
    }

    private void setDescrTextView(String descr){
        if(!descr.isEmpty()){
            descrTextView.setText(descr);
        } else {
            descrTextView.setText("keine weiteren Details");
            descrTextView.setTypeface(null, Typeface.ITALIC);
        }
    }


    //sets child "status" to ACCEPTED (= 1) or DECLINED (= -1) after showing a dialog to confirm decision
    @Override
    public void onClick(View v) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();
        statusRef = rootRef.child(userId).child("Shared Events").child(eventId).child("status");

        switch (v.getId()){
            case R.id.decline_imageView:
                createDeclineDialog();
                break;
            case R.id.accept_imageView:
                createAcceptDialog();
                break;
        }
    }

    private void createDeclineDialog(){
        final Dialog declineDialog = new Dialog(this);
        declineDialog.setContentView(R.layout.dialog_declined);
        dialogDeclinedTextView = declineDialog.findViewById(R.id.dialog_decline_textView);
        dialogDeclinedTextView.setText("Bei Klicken auf \"Okay!\" wird das Event \""+ title +"\" abgelehnt!");
        okayDeclineButton = declineDialog.findViewById(R.id.okay_decline_button);
        okayDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineDialog.dismiss();
                createReasonDialog();
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
    }

    //creates dialog when declining an event to give extra information about decline reason to user who sent in that event
    //decline reason is found in SharedEventsFragment of that user
    private void createReasonDialog(){
        final Dialog reasonDialog = new Dialog(HandleEntryActivity.this);
        reasonDialog.setContentView(R.layout.dialog_decline_reason);
        reasonSpinner = reasonDialog.findViewById(R.id.reason_spinner);
        adapter = ArrayAdapter.createFromResource(HandleEntryActivity.this, R.array.reasons, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(adapter);

        reasonSendButton = reasonDialog.findViewById(R.id.reason_send_button);
        reasonSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusRef.setValue(Constants.DECLINED);
                String reason = reasonSpinner.getSelectedItem().toString();
                rootRef.child(userId).child("Shared Events").child(eventId).child("Decline Reason").setValue(reason);
                reasonDialog.dismiss();
                startActivity(new Intent(HandleEntryActivity.this, MainActivity.class));
                Toast.makeText(HandleEntryActivity.this, "Event \""+ title +"\" wurde abgelehnt!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        reasonDialog.show();
    }


    private void createAcceptDialog(){
        final Dialog acceptDialog = new Dialog(this);
        acceptDialog.setContentView(R.layout.dialog_accepted);
        dialogAcceptedTextView = acceptDialog.findViewById(R.id.dialog_accept_textView);
        dialogAcceptedTextView.setText("Bei Klicken auf \"Okay!\" wird das Event \""+ title +"\" veröffentlicht!");
        okayAcceptButton = acceptDialog.findViewById(R.id.okay_accept_button);
        okayAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusRef.setValue(Constants.ACCEPTED);
                acceptDialog.dismiss();
                startActivity(new Intent(HandleEntryActivity.this, MainActivity.class));
                Toast.makeText(getApplicationContext(), "Event \""+ title +"\" wurde veröffentlicht!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        quitAcceptButton = acceptDialog.findViewById(R.id.quit_accept_button);
        quitAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptDialog.dismiss();
            }
        });

        acceptDialog.show();
    }
}
