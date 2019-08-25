package com.example.meivents;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ShareEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Spinner admissionSpinner;
    ArrayAdapter<CharSequence> adapter;

    EditText inputTitle;
    EditText inputDate;

    Button sendButton;

    SharedEventItem newSharedEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);

        admissionSpinner = findViewById(R.id.admission_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.admissions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        admissionSpinner.setAdapter(adapter);
        admissionSpinner.setOnItemSelectedListener(this);

        inputTitle = findViewById(R.id.title_editText);
        inputDate = findViewById(R.id.when_editText);
        inputDate.setFocusable(false);
        inputDate.setOnClickListener(this);

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedAdmission = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.when_editText:
                createDatePickerDialog().show();
                break;
            case R.id.send_button:
                addToSharedEvents();
        }



    }

    private DatePickerDialog createDatePickerDialog() {
        GregorianCalendar today = new GregorianCalendar();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        return new DatePickerDialog(this, this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(date.getTime());
        inputDate.setText(dateString);
    }

    private void addToSharedEvents(){
        String title = inputTitle.getText().toString().trim();
        String date = inputDate.getText().toString().trim();

        if(!title.isEmpty() && !date.isEmpty()) {
            newSharedEvent = new SharedEventItem(title, date, Constants.WAITING);


        }


    }
}
