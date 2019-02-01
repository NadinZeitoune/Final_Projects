package com.project.nadin.androidproject_rides_clientside_app;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnection {

    public static final int ERROR = 404;
    public static final String RIDES_DELIMITER = "!";

    public static Object connection(String action, Object... obj) {
        URL url = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        String response = String.valueOf(ERROR);

        try {
            // Get basic connection.
            url = new URL("http://10.0.2.2:8080/server?action=" + action);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            // Add body.
            String body = addBody(obj);
            outputStream = connection.getOutputStream();

            outputStream.write(body.getBytes());
            connection.getResponseCode();
            inputStream = connection.getErrorStream();
            if (inputStream == null) {
                inputStream = connection.getInputStream();
            }

            // Choose response.
            switch (action) {
                case "signUp":
                    return signUpResponse(inputStream);
                case "login":
                    return loginResponse(inputStream);
                case "addRide":
                    return addRideResponse(inputStream);
                case "search":
                    return searchRidesResponse(inputStream);
                case "searchDriver":
                    return searchDriverResponse(inputStream);
                case "searchPassenger":
                    return searchPassengerResponse(inputStream);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return Integer.valueOf(response);
    }

    private static Ride[] searchPassengerResponse(InputStream inputStream) throws IOException{
        return searchRidesResponse(inputStream);
    }

    private static Ride[] searchDriverResponse(InputStream inputStream) throws IOException{
        return searchRidesResponse(inputStream);
    }

    private static Ride[] searchRidesResponse(InputStream inputStream) throws IOException {
        // Extract the string from the bytes.
        StringBuilder ridesBuilder = new StringBuilder();
        //inputStream = connection.getInputStream();
        byte[] buffer = new byte[64];
        int actuallyRead;

        try {
            while ((actuallyRead = inputStream.read(buffer)) != -1) {
                ridesBuilder.append(new String(buffer, 0, actuallyRead));
            }

            // Split the string according to specific delimiter.
            String[] parts = ridesBuilder.toString().split(RIDES_DELIMITER);
            System.out.println();
            // Create Ride[] in the length of the parts[] == amount of rides.
            Ride[] rides = new Ride[parts.length];

            // For i loop - create Ride from part and insert to the i position of the Ride[].
            for (int i = 0; i < parts.length; i++) {
                rides[i] = new Ride(parts[i]);
            }

            // send back Ride[]
            return rides;

        } catch (Exception e) {
            return null;
        }
    }

    private static boolean addRideResponse(InputStream inputStream) throws IOException {
        // Read numeric respond from server.
        byte[] buffer = new byte[4];
        int actuallyRead = inputStream.read(buffer);
        if (actuallyRead != -1) {
            try {
                int response = Integer.valueOf(new String(buffer, 0, actuallyRead));
                if (response != ERROR)
                    return true;
                else
                    return false;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private static int signUpResponse(InputStream inputStream) throws IOException {
        // Read numeric respond from server.
        byte[] buffer = new byte[4];
        int actuallyRead = inputStream.read(buffer);
        if (actuallyRead != -1) {
            try {
                return Integer.valueOf(new String(buffer, 0, actuallyRead));
            } catch (Exception e) {
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    private static User loginResponse(InputStream inputStream) throws IOException {
        // Read User respond from server.
        StringBuilder userBuilder = new StringBuilder();

        byte[] buffer = new byte[64];
        int actuallyRead;
        try {
            while ((actuallyRead = inputStream.read(buffer)) != -1) {
                userBuilder.append(new String(buffer, 0, actuallyRead));
            }

            if (userBuilder.equals(ERROR))
                throw new Exception();

            return new User(userBuilder.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private static String addBody(Object... obj) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Object o : obj) {

            // Choose body.
            if (o instanceof User) {
                User user = (User) o;
                stringBuilder.append("bodyUser=" + user.toString());
            }

            if (o instanceof Ride) {
                Ride ride = (Ride) o;
                stringBuilder.append("bodyRide=" + ride.toString());
                stringBuilder.append("&");
            }

            if (o instanceof String) {
                String userName = (String) o;
                stringBuilder.append("bodyUsername=" + userName);
            }

            if (o instanceof JSONObject) {
                JSONObject searchDetails = (JSONObject) o;

                if (searchDetails == null)
                    stringBuilder.append("bodySearch=null");
                else
                    stringBuilder.append("bodySearch=" + searchDetails.toString());
            }

        }

        return stringBuilder.toString();

    }
}
