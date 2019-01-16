package com.project.nadin.androidproject_rides_clientside_app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpFragment extends DialogFragment {
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtPhoneNumber;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnSignUp;
    private OnSignUpFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.txtLastName);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnSignUp = view.findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String phoneNumber = txtPhoneNumber.getText().toString();

                // Check that all fields isn't empty.
                if(isParamsEmpty(userName, password, firstName, lastName, phoneNumber)){
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(listener != null) {
                    // Check if username not taken.


                    // When not taken- create new user and send back the user details.
                    // add to user map.
                    User user = new User(userName, password, firstName, lastName, Integer.valueOf(phoneNumber));

                    // Send back user details.
                    listener.onSignUp(user);
                }

                dismiss();
            }
        });

        return view;
    }

    // Check all params if empty.
    private boolean isParamsEmpty(String... params){
        for (String param : params) {
            if (param.isEmpty())
                return true;
        }
        return false;
    }

    public void setListener(OnSignUpFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnSignUpFragmentListener{
        void onSignUp(User logged);
    }
}
