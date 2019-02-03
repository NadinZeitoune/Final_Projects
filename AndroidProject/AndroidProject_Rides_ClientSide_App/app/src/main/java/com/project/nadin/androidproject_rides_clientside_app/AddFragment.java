package com.project.nadin.androidproject_rides_clientside_app;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

public class AddFragment extends Fragment {
    public static final String ADD_RIDE = "addRide";
    private OnAddFragmentListener listener;

    private TextView lblDeparture;
    private EditText txtOrigin;
    private EditText txtDestination;
    //private EditText txtVia;
    private TextView lblArrival;
    private EditText txtSeats;
    private Button btnAddRide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        lblDeparture = view.findViewById(R.id.lblDeparture);
        txtOrigin = view.findViewById(R.id.txtOrigin);
        txtDestination = view.findViewById(R.id.txtDestination);
        lblArrival = view.findViewById(R.id.lblArrival);
        txtSeats = view.findViewById(R.id.txtSeats);
        btnAddRide = view.findViewById(R.id.btnAddRide);


        btnAddRide.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(final View view) {
                // Get date.
                String departure = lblDeparture.getText().toString();
                String origin = txtOrigin.getText().toString();
                String destination = txtDestination.getText().toString();
                String arrival = lblArrival.getText().toString();
                String seats = txtSeats.getText().toString();

                // Check that all fields are filled.
                if (isParamsEmpty(departure, origin, destination, arrival, seats)) {
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.valueOf(seats) > 5) {
                    // Passengers num MUST be under or equals to 5!
                    Toast.makeText(getContext(), "Max of 5 passengers!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Integer.valueOf(seats) <= 0){
                    // Passengers num MUST be above 0!
                    Toast.makeText(getContext(), "Min of 1 passengers!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (listener != null) {
                    // Lock the button.
                    btnAddRide.setEnabled(false);
                    btnAddRide.setText(R.string.wait_message);

                    // Send details to server
                    new AsyncTask<Ride, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(Ride... rides) {
                            // Connect to server to insert the ride.
                            String driverUsername = getContext().getSharedPreferences(MainActivity.PREFS, Context.MODE_PRIVATE)
                                    .getString(MainActivity.USERNAME, "");

                            boolean isRideAdded = (boolean) HttpConnection.connection(ADD_RIDE, rides[0], driverUsername);
                            return isRideAdded;
                        }

                        @Override
                        protected void onPostExecute(Boolean success) {
                            // Enable the button.
                            btnAddRide.setEnabled(true);
                            btnAddRide.setText(R.string.add_ride);
                            
                            if (success){
                                Toast.makeText(getContext(), "Ride added!", Toast.LENGTH_SHORT).show();
                                listener.onAdd();
                            }else{
                                Toast.makeText(getContext(), "Couldn't add ride. Please check the data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(new Ride(origin, destination, departure, arrival, Integer.valueOf(seats)));
                }
            }
        });

        // Show the date time fragment to the user.
        setDateTime(lblDeparture);
        lblDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTime(lblDeparture);
            }
        });
        lblArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTime(lblArrival);
            }
        });

        return view;
    }

    // Let the user set the date and time.
    private void setDateTime(final View view) {
        DateTimeFragment dateTimeFragment = new DateTimeFragment();
        dateTimeFragment.setListener(new DateTimeFragment.OnDateTimeFragmentListener() {
            @Override
            public void onDateTime(String date, String time) {
                TextView textView = (TextView) view;
                textView.setText(" " + date + ", " + time);
            }
        });
        dateTimeFragment.show(getFragmentManager(), "dateTime");
    }

    // Check all params if empty.
    private boolean isParamsEmpty(String... params) {
        for (String param : params) {
            if (param == null || param.isEmpty())
                return true;
        }
        return false;
    }

    public void setListener(OnAddFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnAddFragmentListener {
        void onAdd();
    }
}
