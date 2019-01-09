package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class RidesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
    }

    public void onLogOut(View view) {
        // restarting the app:
        // clears the logged in user
        // opens the main activity
        // closes this one
    }

    public void onAddRide(View view) {
        // open add ride fragment
        // after finishing the form, the ride will add to the showing list
        // to the "my drives" list - in my settings
    }

    public void onSearchRide(View view) {
        // open search fragment
        // the result of the fragment will effect the showing list of rides.
    }
}
