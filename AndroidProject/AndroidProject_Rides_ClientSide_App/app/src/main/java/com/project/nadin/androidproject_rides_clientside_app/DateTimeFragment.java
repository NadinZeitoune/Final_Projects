package com.project.nadin.androidproject_rides_clientside_app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DateTimeFragment extends DialogFragment {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnDateTime;
    private OnDateTimeFragmentListener listener;
    private String date;
    private String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_date_time, container, false);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        btnDateTime = view.findViewById(R.id.btnDateTime);

        btnDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if user entered only date.
                if (date.isEmpty()){
                    date = formatDate();
                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.VISIBLE);
                }
                // The user entered both date and time.
                else {
                    time = formatTime(timePicker.getHour(), timePicker.getMinute());

                    if (listener != null)
                        listener.onDateTime(date, time);
                    dismiss();
                }
            }
        });

        return view;
    }

    // Get the date in string form.
    private String formatDate(){
        String result = "";
        result += datePicker.getDayOfMonth() + "/";
        result += (datePicker.getMonth() + 1) + "/";
        result += datePicker.getYear();

        return result;
    }

    // Get the time in string form.
    private String formatTime(int hour, int minute){
        String result = "";

        if(hour < 10){
            result += "0";
        }
        result+= hour;
        result+=":";

        if (minute < 10){
            result+="0";
        }
        result +=minute;
        return result;
    }

    public void setListener(OnDateTimeFragmentListener listener) {
        this.listener = listener;
    }

    public interface OnDateTimeFragmentListener{
        void onDateTime(String date, String time);
    }
}
