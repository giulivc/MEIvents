package com.example.meivents;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    Button codeConfirmButton;
    EditText codeEditView;

    final String ACCESS_CODE = "219912";
    boolean isFachschaft = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        codeConfirmButton = view.findViewById(R.id.codeConfirmButton);
        codeConfirmButton.setOnClickListener(this);
        codeEditView = view.findViewById(R.id.codeEditView);

        return view;
    }


    @Override
    public void onClick(View v) {
        checkAccessCode();
    }

    private void checkAccessCode(){
        String input = codeEditView.getText().toString();
        if(input.equals(ACCESS_CODE)) {
            Toast.makeText(getContext(), "Richtig", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), LoginSuccessfulActivity.class);
            startActivity(i);
            isFachschaft = true;
        } else {
            Toast.makeText(getContext(), "Falsch", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isFachschaft(){
        return isFachschaft;
    }
}