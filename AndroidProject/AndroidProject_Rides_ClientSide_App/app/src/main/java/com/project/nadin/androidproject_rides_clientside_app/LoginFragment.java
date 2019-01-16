package com.project.nadin.androidproject_rides_clientside_app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends DialogFragment {
    private String userName;
    private EditText txtUserName;
    private EditText txtPassword;
    private Button btnLogin;
    private OnLoginFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        txtUserName = view.findViewById(R.id.txtUsername);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        if(userName != null) {
            txtUserName.setText(userName);
            txtPassword.requestFocus();
        }else{
            //txtUserName.requestFocus();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                if(userName.isEmpty() || password.isEmpty()){
                    Toast.makeText(getContext(), "username and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(listener != null) {

                    // Check if username and password are correct.

                    // When correct- send back the user details.
                    listener.onLogin(new User("","","","", 0));
                }
                dismiss();
            }
        });

        //this line is responsible of popping up the keyboard
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    public void setListener(OnLoginFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnLoginFragmentListener{
        void onLogin(User logged);
    }
}
