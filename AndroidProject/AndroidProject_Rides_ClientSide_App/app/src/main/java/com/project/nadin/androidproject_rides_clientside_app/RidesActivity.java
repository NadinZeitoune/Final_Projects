package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class RidesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);

        // Show user name.
        Serializable userName = getIntent().getSerializableExtra(MainActivity.USER);
        if (userName != null) {
            User user = (User) userName;
            TextView lblUser = findViewById(R.id.lblUser);
            lblUser.setText(" " + user.getFirstName() + ",");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load user profile from inner file- named as username.

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save profile to file.

    }

    public void onLogOut(View view) {
        // Restarting the app:
        // Save user file to server.
        // Delete user file from device.
        // Clears the logged in user
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);
        prefs.edit().remove(MainActivity.USERNAME).remove(MainActivity.PASSWORD)
                .remove(MainActivity.FIRST_NAME).commit();

        // Opens the main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Closes this one
        finish();
    }

    public void onAddRide(View view) {
        // open add ride fragment
        AddFragment addFragment = new AddFragment();

        addFragment.setListener(new AddFragment.OnAddFragmentListener() {
            @Override
            public void onAdd(Ride newRide) {

            }
        });
        //addFragment.ap

        // after finishing the form, the ride will add to the showing list
        // to the "my drives" list - in my settings
    }

    public void onSearchRide(View view) {
        // open search fragment
        // the result of the fragment will effect the showing list of rides.
    }
}
