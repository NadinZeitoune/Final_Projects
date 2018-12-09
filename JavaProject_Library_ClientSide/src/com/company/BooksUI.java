package com.company;

public class BooksUI {

    public static final String CHILDREN = "Children";
    public static final String FANTASY = "Fantasy";
    public static final String NOVEL = "Novel";
    public static final String POETRY = "Poetry";
    public static final String THRILLER = "Thriller";

    public static void booksActions(){
        // Start books menu loop.
        int choice;

        while ((choice = printBooksMenu()) != 6){
            switch (choice){

                // Add a book.
                case 1:
                    // Select genre before adding the book.
                    selectGenre();
                    break;

                // Delete a book.
                case 2:
                    deleteABook();
                    break;

                // Search a book.
                case 3:
                    searchABook();
                    break;

                // Search genre.
                case 4:
                    selectSearchingGenre();
                    break;

                // Print full list.
                case 5:
                    printFullList();
                    break;
            }
        }

        System.out.println("going back");
    }

    private static int printBooksMenu(){
        System.out.println("Please choose:");
        System.out.println("1. Add a book");
        System.out.println("2. Delete a book");
        System.out.println("3. Search a book");
        System.out.println("4. Search genre books");
        System.out.println("5. Print full book list");
        System.out.println("6. Go back");

        // Read choice from user.
        int choice = Main.readIntegerFromConsole("Your choice: ");

        // Check validation of user choice.
        if (choice < 1 || choice > 6){
            System.out.println("Invalid choice!");
            return printBooksMenu();
        }

        return choice;
    }

    private static void selectGenre(){

        int choice = printGenreMenu();

        switch (choice){
            case 1:
                addBook(CHILDREN);
                break;
            case 2:
                addBook(FANTASY);
                break;
            case 3:
                addBook(NOVEL);
                break;
            case 4:
                addBook(POETRY);
                break;
            case 5:
                addBook(THRILLER);
                break;
        }
    }

    private static int printGenreMenu(){
        System.out.println("Please choose genre:");
        System.out.println("1. Children");
        System.out.println("2. Fantasy");
        System.out.println("3. Novel");
        System.out.println("4. Poetry");
        System.out.println("5. Thriller");
        System.out.println("6. Go back.");

        // Read choice from user.
        int choice = Main.readIntegerFromConsole("Your choice: ");

        // Check validation of user choice.
        if (choice < 1 || choice > 6){
            System.out.println("Invalid choice!");
            return printBooksMenu();
        }

        return choice;
    }

    private static void addBook(String choice){
        // Get book name and author name from user.
        String bookName = Main.readStringFromConsole("Please enter book's name: ");
        String authorName = Main.readStringFromConsole("Please enter author name: ");

        // Create new book.
        Book book = new Book(bookName, authorName, choice);

        // Check if book already exist, add if not.
        Server.addABook(book, new Server.ResultSuccessListener() {
            @Override
            public void onResult(Boolean success) {
                if (success){
                    // Add book.
                    System.out.println("Book added!");
                }
                else
                    System.out.println("Book already exist!");
            }
        });
    }

    private static void deleteABook(){
        // Get book name and author name from user.
        String bookName = Main.readStringFromConsole("Please enter book's name: ");
        String authorName = Main.readStringFromConsole("Please enter author name: ");

        // Create new book.
        Book book = new Book(bookName, authorName, "");

        Server.deleteABook(book, new Server.ResultListener() {
            @Override
            public void onResult(int success) {
                if (success == Server.NULL)
                    System.out.println("The book is loaned! Try again later.");
                else if (success == Server.OKAY)
                    System.out.println("The book deleted!");
                else
                    System.out.println("The book does'nt exist!");
            }
        });
    }

    private static void searchABook(){
        // Get book name and author name from user.
        String bookName = Main.readStringFromConsole("Please enter book's name: ");
        String authorName = Main.readStringFromConsole("Please enter author name: ");

        // Create new book.
        Book book = new Book(bookName, authorName, "");

        Server.searchABook(book, new Server.BookListener() {
            @Override
            public void onBook(Book book) {
                if (book != null)
                    System.out.println(book);
                else
                    System.out.println("There's no such book!");
            }
        });
    }

    private static void selectSearchingGenre(){
        int choice = printGenreMenu();

        switch (choice){
            case 1:
                searchGenre(CHILDREN);
                break;
            case 2:
                searchGenre(FANTASY);
                break;
            case 3:
                searchGenre(NOVEL);
                break;
            case 4:
                searchGenre(POETRY);
                break;
            case 5:
                searchGenre(THRILLER);
                break;
        }
    }

    private static void searchGenre(String genre){
        Server.searchGenre(genre, new Server.BookListener() {
            @Override
            public void onBook(Book book) {
                if (book != null)
                    System.out.println(book);
                else
                    System.out.println("There's no books in this genre!");
            }
        });
    }

    private static void printFullList(){
        Server.printFullList(new Server.BookListener() {
            @Override
            public void onBook(Book book) {
                if (book != null)
                    System.out.println(book);
                else
                    System.out.println("There's no books in the list! please add.");
            }
        });
    }
}
