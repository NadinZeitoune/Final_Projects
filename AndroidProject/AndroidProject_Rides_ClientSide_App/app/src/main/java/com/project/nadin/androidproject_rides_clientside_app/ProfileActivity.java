package com.project.nadin.androidproject_rides_clientside_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {

    private LinearLayout layoutList;
    private ListView lstRides;
    private TextView lblRideTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        layoutList = findViewById(R.id.layoutList);
        lstRides = findViewById(R.id.lstRides);
        lblRideTitle = findViewById(R.id.lblRideTitle);
    }

    public void onMyDriving(View view) {
        // Set to visible the listView of rides.
        layoutList.setVisibility(View.VISIBLE);
        lblRideTitle.setText(R.string.driving_title);

        // Search for all rides that the user is the driver
        // Show the list.
        getRideList("searchDriver");
    }

    public void onMyRides(View view) {

        // Set to visible the listView of rides.
        layoutList.setVisibility(View.VISIBLE);
        lblRideTitle.setText(R.string.rides_title);

        // Search for all rides that the user is one of the passengers.
        // Show the list.
        getRideList("searchPassenger");
    }

    @SuppressLint("StaticFieldLeak")
    private void getRideList(String action){
        // Connect the server and send the action and the username.
        new AsyncTask<String, Void, Ride[]>(){

            @Override
            protected Ride[] doInBackground(String... strings) {
                String userName = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE).getString(MainActivity.USERNAME, "");
                Ride[] rides = (Ride[]) HttpConnection.connection(strings[0], userName);
                return rides;
            }

            @Override
            protected void onPostExecute(Ride[] rides) {
                // Get back list and show it.
                if (rides == null) {
                    rides = new Ride[]{};
                }

                // Add the Ride[] to the listView.
                ArrayAdapter<Ride> adapter = new RidesAdapter(ProfileActivity.this, rides);
                final Ride[] finalRides = rides;
                lstRides.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Open ride activity
                        openRideActivity(finalRides[i]);
                    }
                });
                lstRides.setAdapter(adapter);
                Toast.makeText(ProfileActivity.this, "List refreshed!", Toast.LENGTH_SHORT).show();
            }
        }.execute(action);
    }

    private void openRideActivity(Ride ride) {
        // Open the Ride activity.
        Intent intent = new Intent(this, RideActivity.class);

        // Send the logged data to Rides activity.
        intent.putExtra(RideActivity.RIDE, ride);

        // Close currant activity.
        startActivity(intent);
    }
}
