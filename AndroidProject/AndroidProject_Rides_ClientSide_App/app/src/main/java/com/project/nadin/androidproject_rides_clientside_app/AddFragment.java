package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddFragment extends Fragment {
    private OnAddFragmentListener listener;
    private Button btnAddRide;

    private TextView lblDeparture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        lblDeparture = view.findViewById(R.id.lblDeparture);
        btnAddRide = view.findViewById(R.id.btnAddRide);
        btnAddRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check that all fields are filled.
                // passengers num MUST be under or equals to 5!
                // send details to server

                // send null if something is wrong.
                //listener.onAdd(new Ride("hi", "g", "34", "45",4));
            }
        });

        // Show the date time fragment to the user.
        setDateTime();
        lblDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTime();
            }
        });

        return view;
    }

    // Let the user set the date and time.
    private void setDateTime(){
        DateTimeFragment dateTimeFragment = new DateTimeFragment();
        dateTimeFragment.setListener(new DateTimeFragment.OnDateTimeFragmentListener() {
            @Override
            public void onDateTime(String date, String time) {
                lblDeparture.setText(" " + date + ", " + time);
            }
        });
        dateTimeFragment.show(getFragmentManager(), "dateTime");
    }

    public void setListener(OnAddFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnAddFragmentListener{
        void onAdd(Ride newRide);
    }
}
