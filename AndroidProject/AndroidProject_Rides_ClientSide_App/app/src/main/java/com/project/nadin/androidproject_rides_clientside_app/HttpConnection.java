package com.project.nadin.androidproject_rides_clientside_app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnection {

    public static final int ERROR = 404;

    public static Object connection(String action, Object obj) {
        URL url = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String response = String.valueOf(ERROR);

        try {
            url = new URL("http://10.0.2.2:8080/server?action=" + action);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            addBody(connection, obj);

            connection.connect();
            switch (action) {
                case "signUp":
                    return signUpResponse(inputStream, connection);
                case "login":
                    return loginResponse(inputStream, connection);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
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
            if (connection != null) {
                connection.disconnect();
            }
        }

        return Integer.valueOf(response);
    }

    private static int signUpResponse(InputStream inputStream, HttpURLConnection connection) throws IOException {
        inputStream = connection.getInputStream();
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

    private static User loginResponse(InputStream inputStream, HttpURLConnection connection) throws IOException {
        StringBuilder userBuilder = new StringBuilder();
        inputStream = connection.getInputStream();
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

    private static void addBody(HttpURLConnection connection, Object obj) throws IOException {
        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

        if (obj instanceof User) {
            User user = (User) obj;
            httpRequestBodyWriter.write("body=" + user.toString());
        }


        httpRequestBodyWriter.close();
    }
}
