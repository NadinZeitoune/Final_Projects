package com.project.nadin.androidproject_rides_clientside_app;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private List<Ride> drivingList;
    private List<Ride> ridesList;

    public User(String userName, String password, String firstName, String lastName, int phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public void addDrive(Ride newDrive){
        drivingList.add(0, newDrive);
    }

    public void addRide(Ride newRide){
        ridesList.add(0, newRide);
    }

    public void removeRide(Ride newRide){
        ridesList.remove(newRide);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof User){
            User user = (User) obj;
            return (this.userName == user.userName);
        }

        return false;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
