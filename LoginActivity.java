package com.example.meivents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inputEmail, inputPassword;
    Button signInButton;
    TextView signUpTextView;
    TextView forgotPwTextView;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.login_email_editText);
        inputPassword = findViewById(R.id.login_password_editText);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signUpTextView = findViewById(R.id.sign_up_textView);
        signUpTextView.setOnClickListener(this);
        forgotPwTextView = findViewById(R.id.forgot_pw_textView);
        forgotPwTextView.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null && firebaseUser.isEmailVerified()){
                    finish();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                }
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    progressDialog.setMessage("Anmeldung erfolgt...");
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Anmeldung fehlgeschlagen!", Toast.LENGTH_SHORT).show();
                            } else {
                                checkEmailVerification();
                            }
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(this, "Bitte fülle alle Felder aus!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_up_textView:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.forgot_pw_textView:
                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }


    public void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()){
            finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Bitte verifiziere deine Email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }
    }


}
