package com.project.nadin.androidproject_rides_clientside_app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnection {
    public static void connection(String action){
        URL url = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            url = new URL("http://10.0.2.2:8080/server?action=" + action);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.connect();
            /*inputStream = connection.getInputStream();
            byte[] buffer = new byte[64];
            int actuallyRead = inputStream.read(buffer);
            if(actuallyRead != -1){
                String response = new String(buffer, 0, actuallyRead);
                try{
                    //int result = Integer.valueOf(response);
                    Log.d("nadin", response);
                }catch (Exception e){
                    //throw new Exception("the response from the server was not a number");
                }
            }else{
                //throw new Exception("no response from server");
            }*/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
    }
}
