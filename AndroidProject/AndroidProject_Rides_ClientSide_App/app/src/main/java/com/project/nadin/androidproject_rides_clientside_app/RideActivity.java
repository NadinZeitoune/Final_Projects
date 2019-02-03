package com.project.nadin.androidproject_rides_clientside_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

public class RideActivity extends Activity {

    public static final String RIDE = "Ride";
    public static final String USER_NAME = "userName";
    public static final String USER = "user";

    private TextView lblRideNumber;
    private TextView lblOrigin;
    private TextView lblDestination;
    private TextView lblDeparture;
    private TextView lblArrival;
    private TextView lblSeats;

    private TextView lblDriver;
    private TextView lblPassenger1;
    private TextView lblPassenger2;
    private TextView lblPassenger3;
    private TextView lblPassenger4;
    private TextView lblPassenger5;

    private LinearLayout layoutPassenger1;
    private LinearLayout layoutPassenger2;
    private LinearLayout layoutPassenger3;
    private LinearLayout layoutPassenger4;
    private LinearLayout layoutPassenger5;

    private Button btnLeaveRide;
    private Button btnJoinRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        lblRideNumber = findViewById(R.id.lblRideNumber);
        lblOrigin = findViewById(R.id.lblOrigin);
        lblDestination = findViewById(R.id.lblDestination);
        lblDeparture = findViewById(R.id.lblDeparture);
        lblArrival = findViewById(R.id.lblArrival);
        lblSeats = findViewById(R.id.lblSeats);

        lblDriver = findViewById(R.id.lblDriver);
        lblPassenger1 = findViewById(R.id.lblPassenger1);
        lblPassenger2 = findViewById(R.id.lblPassenger2);
        lblPassenger3 = findViewById(R.id.lblPassenger3);
        lblPassenger4 = findViewById(R.id.lblPassenger4);
        lblPassenger5 = findViewById(R.id.lblPassenger5);

        layoutPassenger1 = findViewById(R.id.layoutPassenger1);
        layoutPassenger2 = findViewById(R.id.layoutPassenger2);
        layoutPassenger3 = findViewById(R.id.layoutPassenger3);
        layoutPassenger4 = findViewById(R.id.layoutPassenger4);
        layoutPassenger5 = findViewById(R.id.layoutPassenger5);

        btnLeaveRide = findViewById(R.id.btnLeaveRide);
        btnJoinRide = findViewById(R.id.btnJoinRide);

        // Get ride.
        Ride ride = (Ride) getIntent().getSerializableExtra(RIDE);
        insertRideDetails(ride);

        String userName = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE).getString(MainActivity.USERNAME, "");

        // Check driver.
        if (ride.getDriver().equals(userName))
            btnJoinRide.setVisibility(View.GONE);


        // Check passenger.
        if (isPassenger(userName, ride.getPassenger1(), ride.getPassenger2(),
                ride.getPassenger3(), ride.getPassenger4(), ride.getPassenger5())){
            btnJoinRide.setVisibility(View.GONE);
            btnLeaveRide.setVisibility(View.VISIBLE);
        }

        // Check amount of passengers.
        switch (ride.getNumOfPassengers()){
            case 1:
                if (ride.getPassenger1() != null && !ride.getPassenger1().isEmpty())
                    btnJoinRide.setVisibility(View.GONE);
                break;
            case 2:
                if (ride.getPassenger2() != null && !ride.getPassenger2().isEmpty())
                    btnJoinRide.setVisibility(View.GONE);
                break;
            case 3:
                if (ride.getPassenger3() != null && !ride.getPassenger3().isEmpty())
                    btnJoinRide.setVisibility(View.GONE);
                break;
            case 4:
                if (ride.getPassenger4() != null && !ride.getPassenger4().isEmpty())
                    btnJoinRide.setVisibility(View.GONE);
                break;
            case 5:
                if (ride.getPassenger5() != null && !ride.getPassenger5().isEmpty())
                    btnJoinRide.setVisibility(View.GONE);
                break;
        }
    }

    public void onClickUser(View view) {
        TextView textView = (TextView) view;
        String userName = textView.getText().toString().replace(" ", "");

        // open user fragment.
        UserFragment userFragment = new UserFragment();

        // send to the fragment the username that clicked.
        Bundle params = new Bundle();
        params.putString(USER_NAME, userName);

        userFragment.setArguments(params);
        userFragment.show(getFragmentManager(), USER);
    }

    @SuppressLint("StaticFieldLeak")
    public void onLeaveRide(View view) {
        int rideNum = Integer.valueOf(lblRideNumber.getText().toString());
        final String userName = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE).getString(MainActivity.USERNAME, "");
        final LinearLayout passengerLayout;

        // Get user passenger layout.
        if (lblPassenger1.getText().toString().equals(" " + userName))
            passengerLayout = layoutPassenger1;
        else if (lblPassenger2.getText().toString().equals(" " + userName))
            passengerLayout = layoutPassenger2;
        else if (lblPassenger3.getText().toString().equals(" " + userName))
            passengerLayout = layoutPassenger3;
        else if (lblPassenger4.getText().toString().equals(" " + userName))
            passengerLayout = layoutPassenger4;
        else
            passengerLayout = layoutPassenger5;

        // Connect to server to delete this user from this ride.
        new AsyncTask<Ride, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Ride... rides) {
                boolean isSuccess = (boolean) HttpConnection.connection("leaveRide", rides[0], userName);

                return isSuccess;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success){
                    // Show join button.
                    btnLeaveRide.setVisibility(View.GONE);
                    btnJoinRide.setVisibility(View.VISIBLE);

                    passengerLayout.setVisibility(View.GONE);
                }else
                    Toast.makeText(RideActivity.this, "Couldn't leave ride! Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }.execute(new Ride(rideNum," "," "," "," ", 0,
                " ", " ", " ", " ", " ", " "));


    }

    @SuppressLint("StaticFieldLeak")
    public void onJoinRide(View view) {
        int rideNum = Integer.valueOf(lblRideNumber.getText().toString());
        // Connect to server to add this user as passenger.
        new AsyncTask<Ride, Void, Boolean>(){
            String userName = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE).getString(MainActivity.USERNAME, "");
            @Override
            protected Boolean doInBackground(Ride... rides) {
                boolean isSuccess = (boolean) HttpConnection.connection("joinRide", rides[0], userName);

                return isSuccess;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success){
                    // Show leave button.
                    btnJoinRide.setVisibility(View.GONE);
                    btnLeaveRide.setVisibility(View.VISIBLE);

                    insertPassengerDetail(userName, layoutPassenger5, lblPassenger5);
                }else
                    Toast.makeText(RideActivity.this, "Couldn't join ride! Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }.execute(new Ride(rideNum," "," "," "," ", 0,
                " ", " ", " ", " ", " ", " "));
    }

    private void insertRideDetails(Ride ride){
        lblRideNumber.setText(String.valueOf(ride.getRideNumber()));
        lblOrigin.setText(" " + ride.getOrigin());
        lblDestination.setText(" " + ride.getDestination());
        lblDeparture.setText(ride.getDeparture());
        lblArrival.setText(ride.getArrival());
        lblSeats.setText(" " + String.valueOf(ride.getNumOfPassengers()));
        lblDriver.setText(" " + ride.getDriver());

        insertPassengerDetail(ride.getPassenger1(), layoutPassenger1, lblPassenger1);
        insertPassengerDetail(ride.getPassenger2(), layoutPassenger2, lblPassenger2);
        insertPassengerDetail(ride.getPassenger3(), layoutPassenger3, lblPassenger3);
        insertPassengerDetail(ride.getPassenger4(), layoutPassenger4, lblPassenger4);
        insertPassengerDetail(ride.getPassenger5(), layoutPassenger5, lblPassenger5);
    }

    private void insertPassengerDetail(String ridePassenger, LinearLayout layout, TextView passenger){
        if (!(ridePassenger == null) && !ridePassenger.isEmpty()){
            layout.setVisibility(View.VISIBLE);
            passenger.setText(" " + ridePassenger);
        }
    }

    private boolean isPassenger(String userName, String... passengers){
        for (String passenger : passengers) {
            if (userName.equals(passenger))
                return true;
        }

        return false;
    }
}
