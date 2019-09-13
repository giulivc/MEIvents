package com.example.meivents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Fragment to enter access code for student council members

public class StudentCouncilLoginFragment extends Fragment implements View.OnClickListener {

    TextInputEditText codeInputEditText;
    Button codeConfirmButton;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_council_login, container, false);

        //sets title in actionbar
        getActivity().setTitle("Fachschafts-Login");

        codeInputEditText = view.findViewById(R.id.code_editText);
        codeConfirmButton = view.findViewById(R.id.codeConfirmButton);
        codeConfirmButton.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = firebaseDatabase.getReference();

        return view;
    }


    @Override
    public void onClick(View v) {
        checkAccessCode();
    }

    //checks if input code is correct
    //if correct starts AuthorizationSuccessfulActivity and sets "Student Council" to true in database
    private void checkAccessCode(){
        String input = codeInputEditText.getText().toString();
        if(input.equals(Constants.ACCESS_CODE)) {
            Intent i = new Intent(getContext(), AuthorizationSuccessfulActivity.class);
            startActivity(i);
            rootRef.child(firebaseAuth.getUid()).child("Student Council").setValue(true);

        } else {
            Toast.makeText(getContext(), "Falscher Zugangscode! Versuch es nochmal!", Toast.LENGTH_SHORT).show();
            codeInputEditText.setText("");
        }
    }
}