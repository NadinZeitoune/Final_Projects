package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLogin(View view) {
        // open login fragment
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setListener(new LoginFragment.OnLoginFragmentListener() {
            @Override
            public void onLogin(User logged) {
                // Save 'logged' to shared prefs.
                openRidesActivity();
            }
        });
        loginFragment.show(getFragmentManager(), "login");
    }

    public void onSignUp(View view) {
        // open sign up fragment
    }

    private void openRidesActivity(){
        // Open the Rides activity.
        Intent intent = new Intent(this, RidesActivity.class);

        // Send the logged data to Rides activity.


        // Close currant activity.
        startActivity(intent);
        finish();
    }

}
