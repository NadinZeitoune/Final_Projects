package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class SearchFragment extends Fragment {

    public static final String DATE_TIME = "dateTime";
    public static final String SEARCH_FILE = "searchDetails.txt";

    private OnSearchFragmentListener listener;
    private Button btnSearchRide;

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

        btnSearchRide = view.findViewById(R.id.btnSearchRide);

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

        // Set btn listener.
        btnSearchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make sure that all checked fields have input.
                CheckBox[] checkBoxes = {chkRideId, chkDeparture, chkArrival, chkOrigin, chkDestination};
                String[] paramsAsString = {txtRideID.getText().toString(), lblDeparture.getText().toString(), lblArrival.getText().toString(),
                        txtOrigin.getText().toString(), txtDestination.getText().toString()};
                if (isCheckedAndEmpty(checkBoxes, paramsAsString)) {
                    Toast.makeText(getContext(), "Please fill the selected!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save in file the details in JSON form.
                JSONObject details = paramsToJSON(paramsAsString, checkBoxes);
                writeJSONToFile(details);

                // Call onSearch method.
                listener.onSearch();
            }
        });

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
                        openDateTimeFragment(lblDeparture);
                    }
                    break;
                case "Arrival time:":
                    lblArrival.setEnabled(checked);

                    if (checked) {
                        lblArrival.setOnClickListener(timeListener);
                        openDateTimeFragment(lblArrival);
                    }
                    break;
                case "Origin point:":
                    txtOrigin.setEnabled(checked);
                    break;
                case "Destination point:":
                    txtDestination.setEnabled(checked);
                    break;
            }
        }

    };

    View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openDateTimeFragment(view);
        }
    };

    private JSONObject paramsToJSON(String[] params, CheckBox[] checkBoxes) {
        // Convert the details to JSONObject.
        JSONObject jsonParams = new JSONObject();
        try {

            putJsonParam(jsonParams, checkBoxes[0], "ride_id", params[0]);
            putJsonParam(jsonParams, checkBoxes[1], "departure", params[1]);
            putJsonParam(jsonParams, checkBoxes[2], "arrival", params[2]);
            putJsonParam(jsonParams, checkBoxes[3], "origin", params[3]);
            putJsonParam(jsonParams, checkBoxes[4], "destination", params[4]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParams;
    }

    private void putJsonParam(JSONObject jsonObject, CheckBox checkBox, String name, String param) throws JSONException {
        if (checkBox.isChecked())
            jsonObject.put(name, param);
        else
            jsonObject.put(name, "");
    }

    private void writeJSONToFile(JSONObject object) {
        OutputStream outputStream = null;

        try {
            outputStream = getContext().openFileOutput(SEARCH_FILE, Context.MODE_PRIVATE);
            outputStream.write(object.toString().getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openDateTimeFragment(final View view) {
        DateTimeFragment dateTimeFragment = new DateTimeFragment();
        dateTimeFragment.setListener(new DateTimeFragment.OnDateTimeFragmentListener() {
            @Override
            public void onDateTime(String date, String time) {
                TextView textView = (TextView) view;
                textView.setText(" " + date + ", " + time);
            }
        });
        dateTimeFragment.show(this.getFragmentManager(), DATE_TIME);
    }

    private boolean isCheckedAndEmpty(CheckBox[] checkBoxes, String[] params) {
        int count = 0;
        for (String param : params) {
            if (checkBoxes[count].isChecked() && (param == null || param.isEmpty()))
                return true;
            count++;
        }
        return false;
    }

    public void setListener(OnSearchFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnSearchFragmentListener {
        void onSearch();
    }
}
