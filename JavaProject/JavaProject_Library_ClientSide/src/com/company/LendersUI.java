package com.company;

public class LendersUI {
    public static void lendersActions(){
        // Start lenders menu loop.
        int choice;

        while ((choice = printLendersMenu()) != 7){
            switch (choice){

                // Search lender.
                case 1:
                    searchLender();
                    break;

                // Add lender.
                case 2:
                    addLender();
                    break;

                // Delete lender
                case 3:
                    deleteLender();
                    break;

                // Lend a book.
                case 4:
                    lendBook();
                    break;

                // Return a book.
                case 5:
                    returnBook();
                    break;

                // Print all lenders.
                case 6:
                    printAllLenders();
                    break;
            }
        }

        System.out.println("going back");
    }

    private static int printLendersMenu(){
        System.out.println("Please choose:");
        System.out.println("1. Search lender");
        System.out.println("2. Add lender");
        System.out.println("3. Delete lender");
        System.out.println("4. Lend a book");
        System.out.println("5. Return a book");
        System.out.println("6. Print all lenders");
        System.out.println("7. Go back");

        // Read choice from user.
        int choice = Main.readIntegerFromConsole("Your choice: ");

        // Check validation of user choice.
        if (choice < 1 || choice > 7){
            System.out.println("Invalid choice!");
            return printLendersMenu();
        }

        return choice;
    }

    private static void searchLender(){
        // Get lender id from user.
        int id  = Main.readIntegerFromConsole("Please enter lender's ID: ");

        // Create fictive lender.
        Lender lender = new Lender("fictive", id, 1);

        Server.searchLender(lender, new Server.LenderListener() {
            @Override
            public void onLender(Lender lender) {
                if (lender != null)
                    System.out.println(lender);
                else
                    System.out.println("There's no such lender!");
            }
        });
    }

    private static void addLender(){
        // Get lender's name, id and phone.
        String lenderName = Main.readStringFromConsole("Please enter lender's name: ");
        int lenderID = Main.readIntegerFromConsole("Please enter lender id: ");
        Long lenderPhone = Main.readLongFromConsole("Please enter lender phone number: ");

        // Create new lender.
        Lender lender = new Lender(lenderName, lenderID, lenderPhone);

        // Check if lender already exist, add if not.
        Server.addLender(lender, new Server.ResultSuccessListener() {
            @Override
            public void onResult(Boolean success) {
                if (success){
                    // Add lender.
                    System.out.println("Lender added!");
                }
                else
                    System.out.println("Lender's ID already exist!");
            }
        });
    }

    private static void deleteLender(){
        // Get lender id from user.
        int id = Main.readIntegerFromConsole("Please enter lender's ID: ");

        // Create fictive lender.
        Lender lender = new Lender("fictive", id, 1);

        Server.deleteLender(lender, new Server.ResultListener() {
            @Override
            public void onResult(int success) {
                if (success == Server.NULL)
                    System.out.println("The lender has loaned books! Try again later.");
                else if (success == Server.OKAY)
                    System.out.println("The lender deleted!");
                else
                    System.out.println("The lender does'nt exist!");
            }
        });
    }

    private static void lendBook(){
        // Get book name and author name from user.
        String bookName = Main.readStringFromConsole("Please enter book's name: ");
        String authorName = Main.readStringFromConsole("Please enter author name: ");

        // Create new book.
        Book book = new Book(bookName, authorName, "");

        // Get lender id from user.
        int id = Main.readIntegerFromConsole("Please enter lender's ID: ");

        // Create fictive lender.
        Lender lender = new Lender("fictive", id, 1);

        Server.lendBook(book, lender, new Server.ResultListener() {
            @Override
            public void onResult(int success) {
                switch (success){
                    case Server.OKAY:
                        System.out.println("Book loaned successfully!");
                        break;
                    case Server.NULL:
                        System.out.println("Can't loan the book. It's already loaned! Try again later.");
                        break;
                    case Server.B_L_NULL:
                        System.out.println("Book not found!");
                        break;
                    case Server.FAILURE:
                        System.out.println("The lender does'nt exist!");
                        break;
                }
            }
        });
    }

    private static void returnBook(){
        // Get book name and author name from user.
        String bookName = Main.readStringFromConsole("Please enter book's name: ");
        String authorName = Main.readStringFromConsole("Please enter author name: ");

        // Create new book.
        Book book = new Book(bookName, authorName, "");

        // Get lender id from user.
        int id = Main.readIntegerFromConsole("Please enter lender's ID: ");

        // Create fictive lender.
        Lender lender = new Lender("fictive", id, 1);

        Server.returnBook(book, lender, new Server.ResultListener() {
            @Override
            public void onResult(int result) {
                switch (result){
                    case Server.OKAY:
                        System.out.println("Book returned successfully!");
                        break;
                    case Server.NULL:
                        System.out.println("Can't return the book. It's loaned to someone else or not loaned at all!");
                        break;
                    case Server.B_L_NULL:
                        System.out.println("Book not found!");
                        break;
                    case Server.FAILURE:
                        System.out.println("The lender does'nt exist!");
                        break;
                }
            }
        });
    }

    private static void printAllLenders() {
        Server.printAllLenders(new Server.LenderListener() {
            @Override
            public void onLender(Lender lender) {
                if (lender != null)
                    System.out.println(lender);
                else
                    System.out.println("There's no lenders on the list! Please add.");
            }
        });
    }
}
