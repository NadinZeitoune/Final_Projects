package com.company;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {

    public static final int LOGIN = 101;
    public static final int ADD_LIBRARIAN = 102;
    public static final int SAVE_DATA = 103;

    public static final int ADD_BOOK = 111;
    public static final int DELETE_BOOK = 112;
    public static final int SEARCH_BOOK = 113;
    public static final int SEARCH_GENRE = 114;
    public static final int PRINT_FULL_LIST = 115;

    public static final int SEARCH_LENDER = 121;
    public static final int ADD_LENDER = 122;
    public static final int DELETE_LENDER = 123;
    public static final int LEND_BOOK = 124;
    public static final int RETURN_BOOK = 125;
    public static final int PRINT_LENDERS = 126;

    public static final int OKAY = 200;
    public static final int NULL = 201;
    public static final int B_L_NULL = 202;
    public static final int FAILURE = 203;
    public static final String LIBRARY_DATA_FILE = "src/com/company/LibraryData.txt";

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private LibraryData libraryData;

    public ClientThread(Socket socket, LibraryData libraryData) {
        this.socket = socket;
        this.libraryData = libraryData;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            // Get what the user want to do.
            int action = Stream.readIntFromStream(inputStream);

            // All relevant user choices.
            switch (action){
                case LOGIN:
                    login();
                    break;
                case ADD_LIBRARIAN:
                    addLibrarian();
                    break;
                case ADD_BOOK:
                    addABook();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case SEARCH_BOOK:
                    searchABook();
                    break;
                case SEARCH_GENRE:
                    searchGenre();
                    break;
                case PRINT_FULL_LIST:
                    printFullList();
                    break;
                case SEARCH_LENDER:
                    searchLender();
                    break;
                case ADD_LENDER:
                    addLender();
                    break;
                case DELETE_LENDER:
                    deleteLender();
                    break;
                case LEND_BOOK:
                    lendBook();
                    break;
                case RETURN_BOOK:
                    returnBook();
                    break;
                case PRINT_LENDERS:
                    printAllLenders();
                    break;
                case SAVE_DATA:
                    saveData();
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // Close transportation.
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void login() throws IOException{
        // Get username and password.
        String username = Stream.readStringFromStream(inputStream);
        String password = Stream.readStringFromStream(inputStream);

        // Check if exist.
        if ((libraryData.librarians.containsKey(username)) &&
                (password.equals(libraryData.librarians.get(username))))
            Stream.writeIntToServer(OKAY, outputStream);
        else{
            // Not exist. Send failure.
            Stream.writeIntToServer(FAILURE, outputStream);
        }
    }

    private void addLibrarian() throws IOException{
        String username =  Stream.readStringFromStream(inputStream);
        String password = Stream.readStringFromStream(inputStream);

        // Check one by one.
        synchronized (libraryData.librarians) {
            if (validatedUser(username)) {
                // Add the details.
                libraryData.addLibrarian(username, password);
                Stream.writeIntToServer(OKAY, outputStream);
            } else
                Stream.writeIntToServer(FAILURE, outputStream);
        }
    }

    private boolean validatedUser(String username){
        // Check if username already exist.
        if (libraryData.librarians.containsKey(username))
            return false;
        return true;
    }

    private void addABook() throws IOException{
        // Get genre.
        Book newBook = new Book(inputStream);
        Book book;

        synchronized (libraryData.bookList){
            // Check if the book exist.
            book = isBookExist(newBook);
            if (book != null)
                Stream.writeIntToServer(FAILURE, outputStream);

            // The book not exist. Add to list.
            else {
                libraryData.bookList.add(newBook);
                Stream.writeIntToServer(OKAY, outputStream);
            }
        }
    }

    private void deleteBook() throws IOException{
        // Get the book.
        Book book = new Book(inputStream);

        synchronized (libraryData.bookList){
            // Check if the book exist.
            book = isBookExist(book);
            if (book != null) {
                // Check if loan.
                if (book.isAvailable()) {
                    libraryData.bookList.remove(book);
                    Stream.writeIntToServer(OKAY, outputStream);
                }
                else
                    Stream.writeIntToServer(NULL, outputStream);
            }
            // The book not exist.
            else
                Stream.writeIntToServer(FAILURE, outputStream);
        }
    }

    private void searchABook() throws IOException{
        // Get book.
        Book book = new Book(inputStream);

        // Search for the book.
        book = libraryData.searchBook(book);

        // If exist- return the book details.
        if (book != null){
            Stream.writeIntToServer(OKAY, outputStream);
            book.write(outputStream);
        }
        else
            Stream.writeIntToServer(FAILURE, outputStream);
    }

    private void searchGenre() throws IOException{
        // Get genre.
        String genre = Stream.readStringFromStream(inputStream);
        int count = 0;

        // Find amount.
        for (Book book : libraryData.bookList) {
            if (book.getType().equals(genre)){
                count++;
            }
        }

        // Send amount.
        Stream.writeIntToServer(count, outputStream);

        // Send genre books.
        for (Book book : libraryData.bookList) {
            if (book.getType().equals(genre)){
                book.write(outputStream);
            }
        }
    }

    private void printFullList() throws IOException{
        // Send amount of books. Loop to check amount.
        Stream.writeIntToServer(libraryData.bookList.size(), outputStream);

        // Send books in loop.
        for (Book book : libraryData.bookList) {
            book.write(outputStream);
        }
    }

    private Book isBookExist(Book newBook){
        // Check if exist.
        for (Book book : libraryData.bookList) {
            if (book.equals(newBook))
                return book;
        }
        return null;
    }

    private void searchLender() throws IOException{
        // Get lender.
        Lender lender = new Lender(inputStream);

        // Search for the lender.
        lender = libraryData.searchLender(lender);

        // If exist- return the lender's details.
        if (lender != null){
            Stream.writeIntToServer(OKAY, outputStream);
            lender.write(outputStream);
        }
        else
            Stream.writeIntToServer(FAILURE, outputStream);
    }

    private void addLender() throws IOException{
        // Get lender.
        Lender newLender = new Lender(inputStream);
        Lender lender;

        synchronized (libraryData.lenderList){
            // Check if the lender exist.
            lender = isLenderExist(newLender);
            if (lender != null)
                Stream.writeIntToServer(FAILURE, outputStream);

            // The lender not exist. Add to list.
            else {
                libraryData.lenderList.add(newLender);
                Stream.writeIntToServer(OKAY, outputStream);
            }
        }
    }

    private void deleteLender() throws IOException{
        // Get the lender.
        Lender lender = new Lender(inputStream);

        synchronized (libraryData.bookList){
            // Check if the book exist.
            lender = isLenderExist(lender);
            if (lender != null) {
                // Check if loaned book list is empty.
                if (lender.getLoanedBook().size() == 0) {
                    libraryData.lenderList.remove(lender);
                    Stream.writeIntToServer(OKAY, outputStream);
                }
                else
                    Stream.writeIntToServer(NULL, outputStream);
            }
            // The lender not exist.
            else
                Stream.writeIntToServer(FAILURE, outputStream);
        }
    }

    private void lendBook() throws IOException{
        // Get lender.
        Lender lender = new Lender(inputStream);
        lender = libraryData.searchLender(lender);

        // Check if lender exist.
        if (lender == null) {
            Stream.writeIntToServer(FAILURE, outputStream);
            return;
        }

        // Get book.
        Book book = new Book(inputStream);
        book = libraryData.searchBook(book);

        // Lender exist. Check if book exist.
        if (book == null){
            Stream.writeIntToServer(B_L_NULL, outputStream);
            return;
        }

        // Book exist. Check if loaned.
        if (!book.isAvailable()){
            // Book loaned.
            Stream.writeIntToServer(NULL, outputStream);
            return;
        }
        // All good. Loan the book.
        else{
            // Change book status.
            book.setAvailable(false);

            // Add book to lender list.
            lender.getLoanedBook().add(book);
            Stream.writeIntToServer(OKAY, outputStream);
        }
    }

    private void returnBook() throws IOException{
        // Get lender.
        Lender lender = new Lender(inputStream);
        lender = libraryData.searchLender(lender);

        // Check if lender exist.
        if (lender == null) {
            Stream.writeIntToServer(FAILURE, outputStream);
            return;
        }

        // Get book.
        Book book = new Book(inputStream);
        book = libraryData.searchBook(book);

        // Lender exist. Check if book exist.
        if (book == null){
            Stream.writeIntToServer(B_L_NULL, outputStream);
            return;
        }

        if (book.isAvailable()){
            // The book is not loaned to anyone.
            Stream.writeIntToServer(NULL, outputStream);
            return;
        }

        boolean exist = false;
        // Book exist. Check if loaned to this lender.
        for (Book loaned : lender.getLoanedBook()) {
            if (book.equals(loaned)){
                exist = true;

                // Get out from loop;
                break;
            }
        }

        // All good. Return the book.
        if (exist) {
            // Change book status.
            book.setAvailable(true);

            // Add book to lender list.
            lender.getLoanedBook().remove(book);

            Stream.writeIntToServer(OKAY, outputStream);
        }else
            // Book not loaned to this lender.
            Stream.writeIntToServer(NULL, outputStream);
    }

    private Lender isLenderExist(Lender newLender) {
        // Check if exist.
        for (Lender lender : libraryData.lenderList) {
            if (lender.equals(newLender))
                return lender;
        }
        return null;
    }

    private void printAllLenders() throws IOException{
        // Send amount of lenders. Loop to check amount.
        Stream.writeIntToServer(libraryData.lenderList.size(), outputStream);

        // Send books in loop.
        for (Lender lender : libraryData.lenderList) {
            lender.write(outputStream);
        }
    }

    private void saveData() throws IOException {
        // Create or override file.
        File file = new File(LIBRARY_DATA_FILE);
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);

            // Write libraryData to file.
            libraryData.writeToFile(fileOutputStream);

            // Send OKAY if all write correctly.
            Stream.writeIntToServer(OKAY, outputStream);
        }
        // Send FAILURE if something wrong.
        catch (Exception e) {
            Stream.writeIntToServer(FAILURE, outputStream);
        }
    }
}
