package com.project.nadin.androidproject_rides_clientside_app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SearchFragment extends DialogFragment {

    public static final String DATE_TIME = "dateTime";

    private OnSearchFragmentListener listener;

    private CheckBox chkRideId;
    private CheckBox chkDeparture;
    private CheckBox chkArrival;
    private CheckBox chkOrigin;
    private CheckBox chkDestination;

    private EditText txtRideID;
    private TextView lblDeparture;
    private TextView lblArrival;
    private EditText txtOrigin;
    private EditText txtDestination;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        chkRideId = view.findViewById(R.id.chkId);
        chkDeparture = view.findViewById(R.id.chkDeparture);
        chkArrival = view.findViewById(R.id.chkArrival);
        chkOrigin = view.findViewById(R.id.chkOrigin);
        chkDestination = view.findViewById(R.id.chkDestination);

        txtRideID = view.findViewById(R.id.txtRideID);
        lblDeparture = view.findViewById(R.id.lblDeparture);
        lblArrival = view.findViewById(R.id.lblArrival);
        txtOrigin = view.findViewById(R.id.txtOrigin);
        txtDestination = view.findViewById(R.id.txtDestination);

        chkRideId.setOnClickListener(chkListener);
        chkDeparture.setOnClickListener(chkListener);
        chkArrival.setOnClickListener(chkListener);
        chkOrigin.setOnClickListener(chkListener);
        chkDestination.setOnClickListener(chkListener);

        return view;
    }

    View.OnClickListener chkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tag = (String) view.getTag();

            CheckBox checkBox = (CheckBox) view;
            Boolean checked = checkBox.isChecked();

            // Check which checkbox is clicked.
            switch (tag) {
                case "Ride ID:":
                    txtRideID.setEnabled(checked);
                    break;
                case "Departure time:":
                    lblDeparture.setEnabled(checked);

                    if (checked) {
                        lblDeparture.setOnClickListener(timeListener);
                        openDateTimeFragment();
                    }
                    break;
                case "Arrival time:":
                    lblArrival.setEnabled(checked);

                    if (checked) {
                        lblArrival.setOnClickListener(timeListener);
                        openDateTimeFragment();
                    }
                    break;
                case "Origin point:":
                    txtOrigin.setEnabled(checked);
                    break;
                case "Destination point:":
                    txtDestination.setEnabled(checked);
                    break;
            }
            dismiss();
        }

    };

    View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openDateTimeFragment();
        }
    };

    private void openDateTimeFragment(){
        DateTimeFragment dateTimeFragment = new DateTimeFragment();
        dateTimeFragment.setListener(new DateTimeFragment.OnDateTimeFragmentListener() {
            @Override
            public void onDateTime(String date, String time) {
                //textView.setText(date + ", " + time);
            }
        });
        dateTimeFragment.show(this.getFragmentManager(), DATE_TIME);
    }

    public void setListener(OnSearchFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnSearchFragmentListener{
        void onSearch();
    }
}
