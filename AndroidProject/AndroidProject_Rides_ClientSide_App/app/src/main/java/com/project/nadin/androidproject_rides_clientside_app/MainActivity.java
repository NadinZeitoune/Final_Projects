package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends Activity {

    public static final String USER = "User";
    public static final String LOGIN = "login";
    public static final String SIGN_UP = "signUp";
    public static final String PREFS = "prefs";
    public static final String LOGGED = "logged";
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if there's already logged in user- there is? Open rides activity.

    }

    public void onLogin(View view) {
        // Open login fragment
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setListener(new LoginFragment.OnLoginFragmentListener() {
            @Override
            public void onLogin(User logged) {
                loggedUser = logged;

                // Save 'logged' to shared prefs.
                //getSharedPreferences(PREFS, MODE_PRIVATE).edit().
                        //putStringSet(LOGGED, loggedUser);

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

                // Save 'logged' to shared prefs.
                openRidesActivity();
            }
        });
        signUpFragment.show(getFragmentManager(), SIGN_UP);
    }

    private void openRidesActivity(){
        // Open the Rides activity.
        Intent intent = new Intent(this, RidesActivity.class);

        // Send the logged data to Rides activity.
        intent.putExtra(USER, loggedUser);

        // Close currant activity.
        startActivity(intent);
        finish();
    }

}
