package com.example.meivents;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//Activity showing new available functions as now being authorized as student council member

public class AuthorizationSuccessfulActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;

    Dialog publishEntriesDialog;

    TextView dialogPublishEntriesTextView;
    Button okayPublishEntriesButton, notPublishEntriesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successful);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();

    }

    @Override
    public void onBackPressed() {
        createPublishEntriesDialog();
    }

    //handles situation that user already sent in events as normal user
    //asks user to share or decline these events by showing a dialog
    private void createPublishEntriesDialog(){
        publishEntriesDialog = new Dialog(this);
        publishEntriesDialog.setContentView(R.layout.dialog_publish_entries);
        dialogPublishEntriesTextView = publishEntriesDialog.findViewById(R.id.dialog_publish_entries_textView);
        dialogPublishEntriesTextView.setText("Bei Klicken auf \"Okay!\" werden alle deine Einsendungen, die noch nicht bearbeitet wurden, veröffentlicht!");

        //searches all sent in events that aren't done yet

        //changes their status to ACCEPTED by clicking on "Okay"
        okayPublishEntriesButton = publishEntriesDialog.findViewById(R.id.okay_publish_entries_button);
        okayPublishEntriesButton.setOnClickListener(this);

        //changes their status to DECLINED by clicking on "Nein, alle ablehnen"
        notPublishEntriesButton = publishEntriesDialog.findViewById(R.id.not_publish_entries_button);
        notPublishEntriesButton.setOnClickListener(this);

        publishEntriesDialog.show();
    }

    @Override
    public void onClick(View v) {
        final DatabaseReference sharedEventsRef = rootRef.child(firebaseAuth.getUid()).child("Shared Events");
        final Query query = sharedEventsRef.orderByChild("status");

        Intent mainIntent = new Intent(AuthorizationSuccessfulActivity.this, MainActivity.class);

        switch (v.getId()){
            case R.id.okay_publish_entries_button:
                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Event event = snapshot.getValue(Event.class);
                                String status = event.getStatus();
                                if(status.equals(Constants.WAITING)){
                                    sharedEventsRef.child(event.getEventId()).child("status").setValue(Constants.ACCEPTED);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                publishEntriesDialog.dismiss();
                startActivity(mainIntent);
                finish();
                break;

            case R.id.not_publish_entries_button:
                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Event event = snapshot.getValue(Event.class);
                                String status = event.getStatus();
                                if(status.equals(Constants.WAITING)){
                                    sharedEventsRef.child(event.getEventId()).child("status").setValue(Constants.DECLINED);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                publishEntriesDialog.dismiss();
                startActivity(mainIntent);
                finish();
                break;
        }
    }
}
