package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.print("Welcome!");

        // Login.
        printLogin();

        // Start Main menu loop.
        int choice;
        while ((choice = printMainMenu()) != 4){
            switch (choice){
                // Lenders actions list.
                case 1:
                    LendersUI.lendersActions();
                    break;

                // Books list.
                case 2:
                    BooksUI.booksActions();
                    break;

                // Add librarian.
                case 3:
                    addLibrarian();
                    break;
            }
        }

        // Save data to file on server.
        saveData();

        // Close client connection to server.
        System.out.println("bye bye");
    }

    private static void printLogin(){
        // Get user name and password from user.
        String user = readStringFromConsole("Please enter your username: ");
        String password = readStringFromConsole("Please enter your password: ");

        // Check if correct.
        Server.login(user, password, new Server.ResultSuccessListener() {
            @Override
            public void onResult(Boolean success) {
                if (success)
                    System.out.println("Login successful!");
                else{
                    System.out.println("Login failed!");
                    printLogin();
                }

            }
        });
    }

    private static int printMainMenu(){
        System.out.println("Please choose:");
        System.out.println("1. Lenders list");
        System.out.println("2. Books list");
        System.out.println("3. Add librarian");
        System.out.println("4. Exit");

        // Read choice from user.
        int choice = readIntegerFromConsole("Your choice: ");

        // Check validation of user choice.
        if (choice < 1 || choice > 4){
            System.out.println("Invalid choice!");
            return printMainMenu();
        }

        return choice;
    }

    private static void addLibrarian(){
        // Get user name and password from user.
        String user = readStringFromConsole("Please enter new username: ");
        String password = readStringFromConsole("Please enter new password: ");

        // Check if username already exist and add if not.
        Server.addLibrarian(user, password, new Server.ResultSuccessListener() {
            @Override
            public void onResult(Boolean success) {
                if (success){
                    // add librarian
                    System.out.println("Librarian added!");
                }
                else{
                    System.out.println("Username already exist. Please pick another one.");
                    addLibrarian();
                }
            }
        });
    }

    private static void saveData(){
        System.out.println("Saving data....");

        // Save...
        Server.saveData(new Server.ResultSuccessListener() {
            @Override
            public void onResult(Boolean success) {
                if (success)
                    // Data saved correctly.
                    System.out.println("Data saved!");
                else
                    System.out.println("Failed to save data!");
            }
        });
    }

    public static String readStringFromConsole(String instruction){
        System.out.print(instruction);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        return input;
    }

    public static int readIntegerFromConsole(String instruction){
        System.out.print(instruction);
        Scanner scanner = new Scanner(System.in);
        String input =  scanner.nextLine();

        int choice = -1;
        try {
            choice = Integer.valueOf(input);
        }catch (Exception ex){
            System.out.println("This is not a number!");
            return readIntegerFromConsole(instruction);
        }
        return choice;
    }

    public static Long readLongFromConsole(String instruction){
        System.out.print(instruction);
        Scanner scanner = new Scanner(System.in);
        String input =  scanner.nextLine();

        long inputNum = -1;
        try {
            inputNum = Long.valueOf(input);
        }catch (Exception ex){
            System.out.println("This is not a number!");
            return readLongFromConsole(instruction);
        }
        return inputNum;
    }
}
