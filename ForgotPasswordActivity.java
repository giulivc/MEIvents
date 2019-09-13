package com.example.meivents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

//Activity to send email to reset password

public class ForgotPasswordActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    EditText inputEmail;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.forgotpw_email_editText);

        resetButton = findViewById(R.id.reset_pw_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Bitte gib deine registrierte E-Mail ein!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Es ist ein Fehler aufgetreten!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "E-Mail wurde gesendet!", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });
    }
}
