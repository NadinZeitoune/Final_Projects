package com.project.nadin.androidproject_rides_clientside_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class RidesActivity extends Activity {

    public static final String SEARCH = "search";
    private User logged;
    private ListView ridesList;

    private FrameLayout fragmentLayout;
    private FragmentManager.OnBackStackChangedListener backStackListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);

        fragmentLayout = findViewById(R.id.fragmentLayout);
        ridesList = findViewById(R.id.lstRides);

        // Show user name.
        Serializable userName = getIntent().getSerializableExtra(MainActivity.USER);

        logged = (User) userName;


        if (logged != null) {
            TextView lblUser = findViewById(R.id.lblUser);
            lblUser.setText(" " + logged.getFirstName() + ",");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load rides from database with details if exist.
        refreshRidesListView(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Delete search file.
        this.deleteFile(SearchFragment.SEARCH_FILE);
    }

    public void onLogOut(View view) {
        // Restarting the app:
        // Clears the logged in user
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);
        prefs.edit().remove(MainActivity.USERNAME).remove(MainActivity.PASSWORD)
                .remove(MainActivity.FIRST_NAME).commit();

        // Opens the main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Closes this one
        finish();
    }

    @SuppressLint("ResourceType")
    public void onAddRide(View view) {
        // Open add ride fragment
        final AddFragment addFragment = new AddFragment();

        getFragmentManager().beginTransaction().add(R.id.fragmentLayout, addFragment)
                .addToBackStack(null).commit();

        // Add the option of getting back with back button.
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                backStackListener = this;

                if (fragmentLayout.getVisibility() == View.GONE)
                    fragmentLayout.setVisibility(View.VISIBLE);
                else {
                    fragmentLayout.setVisibility(View.GONE);
                    removeBackStackListener(addFragment, backStackListener);
                }
            }

        });


        addFragment.setListener(new AddFragment.OnAddFragmentListener() {
            @Override
            public void onAdd() {
                fragmentLayout.setVisibility(FrameLayout.GONE);
                removeBackStackListener(addFragment, backStackListener);

                // After finishing the form, the ride will add to the showing list.
                refreshRidesListView(false);
            }
        });
    }

    public void onSearchRide(View view) {
        // open search fragment
        final SearchFragment searchFragment = new SearchFragment();

        getFragmentManager().beginTransaction().add(R.id.fragmentLayout, searchFragment)
                .addToBackStack(null).commit();

        // Add the option of getting back with back button.
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                backStackListener = this;

                if (fragmentLayout.getVisibility() == View.GONE)
                    fragmentLayout.setVisibility(View.VISIBLE);
                else {
                    fragmentLayout.setVisibility(View.GONE);
                    removeBackStackListener(searchFragment, backStackListener);
                }
            }

        });

        searchFragment.setListener(new SearchFragment.OnSearchFragmentListener() {
            @Override
            public void onSearch() {
                // Close the fragment.
                fragmentLayout.setVisibility(FrameLayout.GONE);
                removeBackStackListener(searchFragment, backStackListener);

                // Refresh rides list according to the details.
                refreshRidesListView(true);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void refreshRidesListView(Boolean isWithDetails) {

        closeKeyboard();

        JSONObject details;

        if (isWithDetails) {
            // Extract details - if file exist. If not, details = null.
            details = readJSONFromFile();

        } else
            details = null;

        // Connect to server and send the details if there are - if false, get ALL rides
        new AsyncTask<JSONObject, Void, Ride[]>() {

            @Override
            protected Ride[] doInBackground(JSONObject... details) {
                Ride[] rides = (Ride[]) HttpConnection.connection(SEARCH, details[0]);
                return rides;
            }

            @Override
            protected void onPostExecute(Ride[] rides) {
                // Get back list and show it.
                if (rides == null) {
                    rides = new Ride[]{};
                }

                // Add the Ride[] to the listView.
                ArrayAdapter<Ride> adapter = new RidesAdapter(RidesActivity.this, rides);
                final Ride[] finalRides = rides;
                ridesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Open ride activity
                        openRideActivity(finalRides[i]);
                    }
                });
                ridesList.setAdapter(adapter);
                Toast.makeText(RidesActivity.this, "List refreshed!", Toast.LENGTH_SHORT).show();
            }
        }.execute(details);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private JSONObject readJSONFromFile() {

        InputStream inputStream = null;

        try {
            inputStream = openFileInput(SearchFragment.SEARCH_FILE);
            byte[] buffer = new byte[1024];
            StringBuilder stringBuilder = new StringBuilder();
            int actuallyRead;
            while ((actuallyRead = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, actuallyRead));
            }
            String response = stringBuilder.toString();
            try {
                JSONObject jsonDetails = new JSONObject(response);
                return jsonDetails;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void removeBackStackListener(Fragment fragment, FragmentManager.OnBackStackChangedListener listener) {
        // Remove the fragment from the backStack.
        getFragmentManager().beginTransaction().remove(fragment).commit();
        getFragmentManager().removeOnBackStackChangedListener(listener);
        backStackListener = null;
    }

    public void onProfileClick(View view) {
        // Open profile activity.
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openRideActivity(Ride ride) {
        // Open the Ride activity.
        Intent intent = new Intent(this, RideActivity.class);

        // Send the logged data to Rides activity.
        intent.putExtra(RideActivity.RIDE, ride);

        // Close currant activity.
        startActivity(intent);
    }
}
