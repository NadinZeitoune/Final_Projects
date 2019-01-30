package com.project.nadin.androidproject_rides_clientside_app;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RidesAdapter extends ArrayAdapter<Ride> {

    private Activity activity;
    private Ride[] rides;

    public RidesAdapter(Activity activity, Ride[] rides){
        super(activity, R.layout.item_ride, rides);
        this.activity = activity;
        this.rides = rides;
    }

    static class ViewContainer{
        TextView lblOrigin, lblDestination, lblDeparture, lblArrival;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewContainer viewContainer;
        if (view == null){
            view = activity.getLayoutInflater().inflate(R.layout.item_ride, parent, false);
            viewContainer = new ViewContainer();
            viewContainer.lblOrigin = view.findViewById(R.id.lblOrigin);
            viewContainer.lblDestination = view.findViewById(R.id.lblDestination);
            viewContainer.lblDeparture = view.findViewById(R.id.lblDeparture);
            viewContainer.lblArrival = view.findViewById(R.id.lblArrival);
            view.setTag(viewContainer);
        }else {
            viewContainer = (ViewContainer) view.getTag();
        }

        Ride ride = rides[position];
        viewContainer.lblOrigin.setText(ride.getOrigin());
        viewContainer.lblDestination.setText(ride.getDestination());
        viewContainer.lblDeparture.setText(ride.getDeparture());
        viewContainer.lblArrival.setText(ride.getArrival());

        return view;
    }
}
