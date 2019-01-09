package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLogin(View view) {
        // open login fragment
    }

    public void onSignUp(View view) {
        // open sign up fragment
    }
}
