package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Book {
    private String bookName;
    private String authorName;
    private String type;
    private boolean isAvailable;

    // Create new book with details.
    public Book(String bookName, String authorName, String type) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.type = type;
        this.isAvailable = true;
    }

    public Book(InputStream inputStream) throws IOException{
        // Get name.
        this.bookName = Stream.readStringFromStream(inputStream);

        // Get author.
        this.authorName = Stream.readStringFromStream(inputStream);

        // Get type.
        this.type = Stream.readStringFromStream(inputStream);

        // Get availability.
        this.isAvailable = Stream.readIntFromStream(inputStream) == 1 ? true : false;
    }

    // Getters for the book details.
    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getType() {
        return type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setter for the available field.
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Write book to stream.
    public void write(OutputStream outputStream) throws IOException{
        // Send length and book name.
        Stream.writeStringToServer(this.bookName, outputStream);

        // Send length and author name.
        Stream.writeStringToServer(this.authorName, outputStream);

        // Send length and type.
        Stream.writeStringToServer(this.type, outputStream);

        // Send boolean.
        Stream.writeIntToServer((this.isAvailable ? 1 : 0), outputStream);
    }

    // Check equality.
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if(obj == this)
            return true;
        if (obj instanceof Book){
            Book other = (Book)obj;
            return this.authorName.equals(other.authorName) && this.bookName.equals(other.bookName);
        }
        return false;
    }

    // Print the book details.
    @Override
    public String toString() {
        return "Book name: " + bookName + ". \n" +
                "Author name: " + authorName + ". \n" +
                "Type: " + type + ". \n" +
                "Status: " + (isAvailable ? "available." : "not available.") + "\n";
    }

}