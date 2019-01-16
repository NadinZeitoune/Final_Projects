package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends Activity {

    public static final String LOGIN = "login";
    public static final String SIGN_UP = "signUp";

    public static final String USER = "User";

    public static final String PREFS = "prefs";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstName";

    private User loggedUser;
    private String loggedUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if there's already logged in user- there is? Open rides activity.
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (prefs.contains(USERNAME)){
            loggedUserName = prefs.getString(FIRST_NAME, "");

            loggedUser = new User(prefs.getString(USERNAME, ""), prefs.getString(PASSWORD, ""),
                   loggedUserName , "", 0);

            openRidesActivity();
        }
    }

    public void onLogin(View view) {
        // Open login fragment
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setListener(new LoginFragment.OnLoginFragmentListener() {
            @Override
            public void onLogin(User logged) {
                loggedUser = logged;
                loggedUserName = logged.getFirstName();

                // Save 'logged' to shared prefs.
                saveLoggedToSharedPrefs();

                openRidesActivity();
            }
        });
        loginFragment.show(getFragmentManager(), LOGIN);
    }

    public void onSignUp(View view) {
        // Open sign up fragment
        SignUpFragment signUpFragment = new SignUpFragment();
        signUpFragment.setListener(new SignUpFragment.OnSignUpFragmentListener() {
            @Override
            public void onSignUp(User logged) {
                loggedUser = logged;
                loggedUserName = logged.getFirstName();

                // Save 'logged' to shared prefs.
                saveLoggedToSharedPrefs();
                openRidesActivity();
            }
        });
        signUpFragment.show(getFragmentManager(), SIGN_UP);
    }

    private void saveLoggedToSharedPrefs() {
        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putString(USERNAME, loggedUser.getUserName())
                .putString(PASSWORD, loggedUser.getPassword())
                .putString(FIRST_NAME, loggedUserName)
                .commit();
    }

    private void openRidesActivity() {
        // Open the Rides activity.
        Intent intent = new Intent(this, RidesActivity.class);

        // Send the logged data to Rides activity.
        intent.putExtra(USER, loggedUser);

        // Close currant activity.
        startActivity(intent);
        finish();
    }

}
