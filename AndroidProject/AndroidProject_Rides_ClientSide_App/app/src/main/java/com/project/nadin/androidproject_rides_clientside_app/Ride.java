package com.project.nadin.androidproject_rides_clientside_app;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Ride {
    public static final String DELIMITER = "&";
    public static final String RIDE_DELIMITER = "%";
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

    public Ride(String rideAsString){
        if (rideAsString == null)
            throw new InvalidParameterException();
        String[] parts = rideAsString.split(RIDE_DELIMITER);

        if (parts.length != 9)
            throw new InvalidParameterException();

        // Ride details.
        this.rideNumber = Integer.valueOf(parts[0]);
        this.origin = parts[1];
        this.destination = parts[2];
        this.departure = parts[3];
        this.arrival = parts[4];
        this.numOfPassengers = Integer.valueOf(parts[5]);
        this.freeSeatsNum = Integer.valueOf(parts[6]);

        // Driver details.
        this.driver = new User(parts[7], true);

        // Passengers details.
        String[] usersParts = parts[8].split(User.USER_DELIMITER);
        this.passengers = User.usersFromString(usersParts);
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

    public static String rideListToString(List<Ride> rides){
        StringBuilder ridesAsString = new StringBuilder();

        for (Ride ride : rides) {
            ridesAsString.append(ride.toString()).append(DELIMITER);
        }

        return ridesAsString.toString();
    }

    @Override
    public String toString() {
        StringBuilder rideAsString = new StringBuilder();

        // Ride details.
        rideAsString.append(rideNumber).append(RIDE_DELIMITER);
        rideAsString.append(origin).append(RIDE_DELIMITER);
        rideAsString.append(destination).append(RIDE_DELIMITER);
        rideAsString.append(departure).append(RIDE_DELIMITER);
        rideAsString.append(arrival).append(RIDE_DELIMITER);
        rideAsString.append(numOfPassengers).append(RIDE_DELIMITER);
        rideAsString.append(freeSeatsNum).append(RIDE_DELIMITER);

        // Driver details.
        rideAsString.append(driver.userProfileToString()).append(RIDE_DELIMITER);

        // Passengers details.
        rideAsString.append(User.usersProfileToString(passengers)).append(RIDE_DELIMITER);

        return rideAsString.toString();
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
