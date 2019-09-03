package com.example.meivents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputEmail, inputPassword;
    Button signUpButton;
    TextView signInTextView;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.register_email_editText);
        inputPassword = findViewById(R.id.register_password_editText);
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);
        signInTextView = findViewById(R.id.sign_in_textView);
        signInTextView.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_up_button:
                final String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registrierung nicht erfolgreich!", Toast.LENGTH_SHORT).show();
                            } else {
                                sendEmailVerification();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_in_textView:
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                break;
        }
    }

    private void sendEmailVerification(){
        progressDialog.setMessage("Verifizierungs-Mail wird gesendet...");
        progressDialog.show();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Verifizierungs-Mail konnte nicht gesendet werden!", Toast.LENGTH_SHORT).show();
                    } else {
                        createUserInDatabase();
                        Toast.makeText(RegisterActivity.this, "Registrierung erfolgreich, bitte verifiziere nun deine Email!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
            });
        }
    }

    private void createUserInDatabase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        UserProfile userProfile = new UserProfile(firebaseAuth.getCurrentUser().getUid());
        rootRef.child(userProfile.getId()).setValue(userProfile);
        rootRef.child(userProfile.getId()).child("Fachschaftsmitglied").setValue(false);

    }

}
