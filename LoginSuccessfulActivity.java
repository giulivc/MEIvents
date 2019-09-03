package com.example.meivents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginSuccessfulActivity extends AppCompatActivity implements View.OnClickListener {

    TextView createEventTextView;
    TextView showEntriesTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successful);

        createEventTextView = findViewById(R.id.create_event_textView);
        showEntriesTextView = findViewById(R.id.show_entries_textView);

        createEventTextView.setOnClickListener(this);
        showEntriesTextView.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginSuccessfulActivity.this, MainActivity.class);
        startActivity(i);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_event_textView:
                break;
            case R.id.show_entries_textView:
                break;
        }
    }
}
