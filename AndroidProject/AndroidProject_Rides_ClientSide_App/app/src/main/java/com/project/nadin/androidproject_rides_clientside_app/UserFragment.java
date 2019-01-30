package com.project.nadin.androidproject_rides_clientside_app;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserFragment extends DialogFragment {

    TextView lblUserName, lblUserPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        lblUserName = view.findViewById(R.id.lblUserName);
        lblUserPhone = view.findViewById(R.id.lblUserPhone);

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
