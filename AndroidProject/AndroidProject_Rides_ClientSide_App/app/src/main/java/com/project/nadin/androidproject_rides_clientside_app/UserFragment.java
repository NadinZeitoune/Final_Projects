package com.project.nadin.androidproject_rides_clientside_app;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserFragment extends DialogFragment {

    public static final String SEARCH_USER = "searchUser";
    TextView lblUserName, lblUserPhone;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        lblUserName = view.findViewById(R.id.lblUserName);
        lblUserPhone = view.findViewById(R.id.lblUserPhone);

        // Get the username that sent here.
        Bundle params = getArguments();
        final String userName = params.getString(RideActivity.USER_NAME);

        // connect the Sql table for getting user details.
        new AsyncTask<String, Void, User>(){
            @Override
            protected User doInBackground(String... strings) {
                User user = (User) HttpConnection.connection(SEARCH_USER, strings[0]);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                // show the details.
                lblUserName.setText(" " + user.getFirstName() + " " + user.getLastName() + ".");
                lblUserPhone.setText(String.valueOf(user.getPhoneNumber()));
            }
        }.execute(userName);



        // Add listener to the phone that open the Dial_App.
        lblUserPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Opens phone app with the number that clicked.
                TextView dial = (TextView) view;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + dial.getText()));
                startActivity(intent);
            }
        });

        return view;
    }
}
