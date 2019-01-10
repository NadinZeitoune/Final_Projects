package com.project.nadin.androidproject_rides_clientside_app;

import java.util.ArrayList;

public class Ride {
    private int rideNumber;
    private String origin;
    private String destination;
    private String departure;
    private String arrival;
    private int freeSeatsNum;
    private int numOfPassengers;

    private User driver;
    private User[] passengers;

    public Ride(String origin, String destination, String departure, String arrival, int numOfPassengers, User driver) {
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.numOfPassengers = numOfPassengers;
        freeSeatsNum = numOfPassengers;

        this.driver = driver;
        passengers = new User[numOfPassengers];
    }

    // Add passenger.
    private void addPassenger(User newPassenger){
        // Add passenger to ride.
        passengers[numOfPassengers - freeSeatsNum] = newPassenger;
        freeSeatsNum--;

        // Add ride to passenger list.
        newPassenger.addRide(this);
    }

    // Delete passenger.
    private void deletePassenger(User newPassenger){
        // Run on all the existing passengers.
        for (int i = 0; i < numOfPassengers - freeSeatsNum; i++) {

            // Check if the passenger exist.
            if (passengers[i].equals(newPassenger)) {

                // Override the passenger with the others in the array.
                for (int j = i; j < numOfPassengers - freeSeatsNum; j++) {
                    passengers[i] = passengers[i + 1];
                }

                // Increase number of free seats.
                freeSeatsNum++;

                // Delete ride from passenger list.
                newPassenger.removeRide(this);
                return;
            }
        }
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public User[] getPassengers() {
        return passengers;
    }

    public void setPassengers(User[] passengers) {
        this.passengers = passengers;
    }
}
