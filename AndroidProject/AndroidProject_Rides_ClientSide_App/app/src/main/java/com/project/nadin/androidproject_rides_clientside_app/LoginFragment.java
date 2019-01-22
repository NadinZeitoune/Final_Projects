package com.project.nadin.androidproject_rides_clientside_app;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends DialogFragment {
    public static final String LOGIN = "login";
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
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                final String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                if(userName.isEmpty() || password.isEmpty()){
                    Toast.makeText(getContext(), "username and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(listener != null) {
                    // Lock the button.
                    btnLogin.setEnabled(false);
                    btnLogin.setText(R.string.wait_message);

                    // Check if username and password are correct.
                    new AsyncTask<User, Void, Boolean>(){
                        User user;

                        @Override
                        protected Boolean doInBackground(User... users) {
                            // Save the user.
                            user = users[0];

                            // Connect to server to check if username exist and password correct.
                            //HttpConnection.connection(LOGIN);
                            // If exist and correct - put user first name, return true
                            //user.setFirstName();

                            // If not - return false
                            return true;//false;
                        }

                        @Override
                        protected void onPostExecute(Boolean success) {
                            // Enable the button.
                            btnLogin.setEnabled(true);
                            btnLogin.setText(R.string.login);

                            // Username+password correct - Log in the user.
                            if (success){
                                // Send back user details.
                                listener.onLogin(user);
                                dismiss();
                            }else{
                                Toast.makeText(getContext(), "Username or password incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(new User(userName,password,"","", 0));
                }
            }
        });

        //  Pop up the keyboard.
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
