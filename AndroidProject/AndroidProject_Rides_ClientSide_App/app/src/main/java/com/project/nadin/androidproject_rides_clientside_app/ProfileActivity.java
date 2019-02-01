package com.project.nadin.androidproject_rides_clientside_app;

import android.annotation.SuppressLint;
import android.app.Activity;
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
    private LinearLayout layoutSetting;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtPhoneNumber;
    private EditText txtPassword;

    private LinearLayout layoutList;
    private ListView lstRides;
    private TextView lblRideTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        layoutSetting = findViewById(R.id.layoutSetting);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtPassword = findViewById(R.id.txtPassword);

        layoutList = findViewById(R.id.layoutList);
        lstRides = findViewById(R.id.lstRides);
        lblRideTitle = findViewById(R.id.lblRideTitle);
    }

    public void onMyDriving(View view) {
        // Set to gone the editProfile layout.
        layoutSetting.setVisibility(View.GONE);

        // Set to visible the listView of rides.
        layoutList.setVisibility(View.VISIBLE);
        lblRideTitle.setText(R.string.driving_title);

        // Search for all rides that the user is the driver
        // Show the list.
        getRideList("searchDriver");
    }

    public void onMyRides(View view) {
        // Set to gone the editProfile layout.
        layoutSetting.setVisibility(View.GONE);

        // Set to visible the listView of rides.
        layoutList.setVisibility(View.VISIBLE);
        lblRideTitle.setText(R.string.rides_title);

        // Search for all rides that the user is one of the passengers.
        // Show the list.
        getRideList("searchPassenger");
    }

    public void onEditProfile(View view) {
        // Set to gone the listView of rides.
        layoutList.setVisibility(View.GONE);

        // Set to visible the editProfile layout.
        layoutSetting.setVisibility(View.VISIBLE);
    }

    public void onProfileChanged(View view) {
        // save the changes.
        // close the setting layout.
        layoutSetting.setVisibility(View.GONE);
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
                lstRides.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Open ride activity
                        Toast.makeText(ProfileActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                lstRides.setAdapter(adapter);
                Toast.makeText(ProfileActivity.this, "List refreshed!", Toast.LENGTH_SHORT).show();
            }
        }.execute(action);
    }
}
