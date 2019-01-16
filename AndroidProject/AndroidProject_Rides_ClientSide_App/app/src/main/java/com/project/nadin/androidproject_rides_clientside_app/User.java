package com.project.nadin.androidproject_rides_clientside_app;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;

public class User implements Serializable {
    public static final String DELIMITER = "$";
    public static final String USER_DELIMITER = "#";
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

    public User(String userAsString){
        if (userAsString == null)
            throw new InvalidParameterException();

        String[] parts = userAsString.split(DELIMITER);
        if (parts.length != 7)
            throw new InvalidParameterException();

        this.userName = parts[0];
        this.password = parts[1];
        this.firstName = parts[2];
        this.lastName = parts[3];
        this.phoneNumber = Integer.valueOf(parts[4]);


    }

    public User(String userAsString, boolean isProfile){
        if (userAsString == null)
            throw new InvalidParameterException();

        String[] parts = userAsString.split(DELIMITER);
        if (parts.length != 3)
            throw new InvalidParameterException();

        this.firstName = parts[0];
        this.lastName = parts[1];
        this.phoneNumber = Integer.valueOf(parts[2]);
    }

    public static User[] usersFromString(String[] usersAsString){
        User[] users = new User[usersAsString.length];
        int i = 0;

        for (String user : usersAsString) {
            String[] parts = user.split(DELIMITER);

            if (parts.length != 3)
                throw new InvalidParameterException();

            users[i] = new User("","", parts[0],
                                parts[1], Integer.valueOf(parts[2]));
            i++;
        }

        return users;
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

    public String userProfileToString(){
        return firstName + DELIMITER + lastName + DELIMITER + phoneNumber;
    }

    public static String usersProfileToString(User[] users){
        StringBuilder usersAsString = new StringBuilder();

        for (int i = 0; i < users.length; i++) {
            User user = users[i];
            usersAsString.append(user.userProfileToString()).append(USER_DELIMITER);
        }

        return usersAsString.toString();
    }

    @Override
    public String toString() {
        StringBuilder userAsString = new StringBuilder();

        // ToString user profile- user details.
        userAsString.append(userName).append(DELIMITER);
        userAsString.append(password).append(DELIMITER);
        userAsString.append(firstName).append(DELIMITER);
        userAsString.append(lastName).append(DELIMITER);
        userAsString.append(phoneNumber).append(DELIMITER);

        // User driving and rides.
        userAsString.append(Ride.rideListToString(drivingList)).append(DELIMITER);
        userAsString.append(Ride.rideListToString(ridesList)).append(DELIMITER);

        return userAsString.toString();
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
