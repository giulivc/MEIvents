package com.example.meivents;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

//Activity to sent in events to student council members (found in EntriesFragment)
//provides fields for each necessary or optional information about event

public class ShareEventActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText inputTitle;
    EditText inputDate;
    EditText inputTime;
    EditText inputPlace;
    EditText inputDescription;

    Spinner admissionSpinner;
    ArrayAdapter<CharSequence> adapter;

    Button sendButton;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);

        inputTitle = findViewById(R.id.title_editText);

        inputDate = findViewById(R.id.date_editText);
        inputDate.setFocusable(false);
        inputDate.setOnClickListener(this);

        inputTime = findViewById(R.id.time_editText);
        inputTime.setFocusable(false);
        inputTime.setOnClickListener(this);

        inputPlace = findViewById(R.id.place_editText);

        //implements spinner to select admission
        admissionSpinner = findViewById(R.id.admission_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.admissions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        admissionSpinner.setAdapter(adapter);

        inputDescription = findViewById(R.id.description_editText);

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_editText:
                createDatePickerDialog().show();
                break;
            case R.id.time_editText:
                createTimePickerDialog().show();
                break;
            case R.id.send_button:
                addEvent();
                break;
        }
    }

    //creates graphical calendar to pick a date
    private DatePickerDialog createDatePickerDialog() {
        GregorianCalendar today = new GregorianCalendar();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        return new DatePickerDialog(this, this, year, month, day);
    }

    //sets date as string in inputDate field
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(date.getTime());
        inputDate.setText(dateString);
    }

    //creates graphical clock to pick a time
    private TimePickerDialog createTimePickerDialog() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(this, this, hour, min, true);
    }

    //sets date as string in inputTime field
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(hourOfDay < 10 && minute < 10){
            inputTime.setText("0" + hourOfDay + ":0" + minute + " Uhr");
        } else if(hourOfDay < 10 && minute >= 10){
            inputTime.setText("0" + hourOfDay + ":" + minute + " Uhr");
        } else if(hourOfDay >=10 && minute < 10){
            inputTime.setText(hourOfDay + ":0" + minute + " Uhr");
        } else {
            inputTime.setText(hourOfDay + ":" + minute + " Uhr");
        }
    }

    //adds event to database with status WAITING to be shown in SharedEventFragment and EntriesFragment
    private void addEvent(){
        String id = UUID.randomUUID().toString();
        String title = inputTitle.getText().toString();
        String date = inputDate.getText().toString();
        String time = inputTime.getText().toString();
        String place = inputPlace.getText().toString();
        String description = inputDescription.getText().toString();
        String admission = admissionSpinner.getSelectedItem().toString();

        if(!title.isEmpty() && !date.isEmpty() && !time.isEmpty() && !place.isEmpty()){
            Event event = new Event(firebaseAuth.getUid(), id, title, date, time, place, admission, description, Constants.WAITING);

            DatabaseReference rootRef = firebaseDatabase.getReference();
            rootRef.child(firebaseAuth.getUid()).child("Shared Events").child(event.getEventId()).setValue(event);
            progressDialog.setMessage("Sende Event...");
            progressDialog.show();
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            Toast.makeText(this, "Bitte fülle alle Pflichtfelder aus!", Toast.LENGTH_SHORT).show();
        }
    }
}
