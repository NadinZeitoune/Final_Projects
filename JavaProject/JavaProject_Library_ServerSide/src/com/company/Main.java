package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static final int PORT = 1996;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        InputStream inputStream = null;

        try {
	        // Search for file. create data file IF NOT EXIST!.
            LibraryData libraryData;
            if (fileExist()){
                File file = new File(ClientThread.LIBRARY_DATA_FILE);
                inputStream = new FileInputStream(file);
                libraryData = new LibraryData(inputStream);
            }
            else{
                // Not exist. Create new.
                libraryData = new LibraryData();
            }

            serverSocket = new ServerSocket(PORT);

            // start loop to connect client.
            while (true){
                Socket socket = serverSocket.accept();
                new ClientThread(socket, libraryData).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean fileExist(){

        File directory = new File("src/com/company");

        for (File file : directory.listFiles()){
            if(file.getName().equals("LibraryData.txt")){
                return true;
            }
        }

        return false;
    }
}
