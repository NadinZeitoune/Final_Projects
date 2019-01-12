package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLogin(View view) {
        // open login fragment
        LoginFragment loginFragment = new LoginFragment();
        //loginFragment.setCancelable(false);
        loginFragment.setTitle("Please login:");
        loginFragment.setListener(new LoginFragment.OnLoginFragmentListener() {
            @Override
            public void onLogin(String userName, String password) {
                Toast.makeText(MainActivity.this, "username: " + userName
                        + ", password: " + password, Toast.LENGTH_SHORT).show();
            }
        });
        loginFragment.show(getFragmentManager(), "login");
    }

    public void onSignUp(View view) {
        // open sign up fragment
    }
}
