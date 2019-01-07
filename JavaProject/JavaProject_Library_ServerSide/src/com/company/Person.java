package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Person {
    private String name;
    private int id;
    private long phoneNumber;

    // Create new person with details.
    public Person(String name, int id, long phoneNumber) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public Person(InputStream inputStream) throws IOException {
        // Get name.
        this.name = Stream.readStringFromStream(inputStream);

        // Get id.
        this.id = Stream.readIntFromStream(inputStream);

        // Get phone number.
        this.phoneNumber = Stream.readLongFromStream(inputStream);
    }

    // Getters and setters for the details.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void write(OutputStream outputStream) throws IOException{
        // Send length and name.
        Stream.writeStringToServer(this.name, outputStream);

        // Send id.
        Stream.writeIntToServer(this.id, outputStream);

        // Send phone number.
        Stream.writeLongToServer(this.phoneNumber, outputStream);
    }

    // Check equality.
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if(obj == this)
            return true;
        if (obj instanceof Person){
            Person other = (Person) obj;
            return this.id == other.id;
        }
        return false;
    }

    // Print the person details.
    @Override
    public String toString() {
        return "Name: " + name + ", ID: " + id + ", Phone number: " + phoneNumber + ".";
    }
}
