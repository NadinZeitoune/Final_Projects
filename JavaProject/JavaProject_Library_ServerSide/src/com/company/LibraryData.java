package com.company;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class LibraryData {
    public List<Book> bookList = null;
    public List<Lender> lenderList = null;
    public Map<String, String> librarians = null;

    public LibraryData() {
        bookList = new ArrayList<>();
        lenderList = new ArrayList<>();
        librarians = new HashMap();

        addLibrarian("manager", "12345");
    }

    // Create data from file.
    public LibraryData(InputStream inputStream) throws IOException{
        librarians = new HashMap();
        bookList = new ArrayList<>();
        lenderList = new ArrayList<>();

        // Create librarians list.
        int amount = Stream.readIntFromStream(inputStream);
        String username;
        String password;

        for (int i = 0; i < amount; i++) {
            // Get data from stream.
            username = Stream.readStringFromStream(inputStream);

            password = Stream.readStringFromStream(inputStream);

            // Create.
            librarians.put(username, password);
        }


        // Create books list.
        amount = Stream.readIntFromStream(inputStream);

        for (int i = 0; i < amount; i++) {
            bookList.add(new Book(inputStream));
        }

        // Create lenders list.
        amount = Stream.readIntFromStream(inputStream);

        for (int i = 0; i < amount; i++) {
            lenderList.add(new Lender(inputStream));
        }
    }

    // Add librarian to list
    public void addLibrarian(String name, String password){
        librarians.put(name, password);
    }

    // Return book that searched.
    public Book searchBook(Book bookToFind){
        for (Book book : bookList) {
            if (bookToFind.equals(book))
                return book;
        }
        return null;
    }

    // Return lender that searched.
    public Lender searchLender(Lender lenderToFind){
        for (Lender lender : lenderList) {
            if (lenderToFind.equals(lender))
                return lender;
        }
        return null;
    }

    // Write To file.
    public void writeToFile(OutputStream outputStream) throws IOException {
        // Write librarians list.
        writeLibrariansToFile(outputStream);

        // Write books list.
        Stream.writeIntToServer(bookList.size(), outputStream);
        for (Book book : bookList) {
            book.write(outputStream);
        }

        // Write lenders list.
        Stream.writeIntToServer(lenderList.size(), outputStream);
        for (Lender lender : lenderList) {
            lender.write(outputStream);
        }
    }

    private void writeLibrariansToFile(OutputStream outputStream) throws IOException{
        Set<String> users = librarians.keySet();

        // Send amount of users.
        Stream.writeIntToServer(users.size(), outputStream);

        // Send users.
        for (String user : users) {
            // Send username.
            Stream.writeStringToServer(user, outputStream);

            // Send password.
            Stream.writeStringToServer(librarians.get(user), outputStream);
        }
    }
}