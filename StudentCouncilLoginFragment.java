package com.example.meivents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentCouncilLoginFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {


    Button codeConfirmButton;
    TextInputEditText codeInputEditText;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_council_login, container, false);

        codeConfirmButton = view.findViewById(R.id.codeConfirmButton);
        codeConfirmButton.setOnClickListener(this);
        codeInputEditText = view.findViewById(R.id.code_editText);

        return view;
    }


    @Override
    public void onClick(View v) {
        checkAccessCode();
    }

    private void checkAccessCode(){
        String input = codeInputEditText.getText().toString();
        if(input.equals(Constants.ACCESS_CODE)) {
            Intent i = new Intent(getContext(), LoginSuccessfulActivity.class);
            startActivity(i);
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference rootRef = firebaseDatabase.getReference();
            rootRef.child(firebaseAuth.getUid()).child("Fachschaftsmitglied").setValue(true);

        } else {
            Toast.makeText(getContext(), "Falscher Zugangscode! Versuch es nochmal!", Toast.LENGTH_SHORT).show();
            codeInputEditText.setText("");
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER) {
            hideSoftKeyboard(getActivity());
            return true;
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}