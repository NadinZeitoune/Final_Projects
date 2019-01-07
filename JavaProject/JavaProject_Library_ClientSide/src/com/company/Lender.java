package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Lender extends Person {
    private List<Book> loanedBook;

    // Create new lender using the parent method.
    public Lender(String name, int id, long phoneNumber) {
        super(name, id, phoneNumber);
        loanedBook = new ArrayList<>();
    }

    // Create lender using stream.
    public Lender(InputStream inputStream) throws IOException{
        super(inputStream);

        // Create the loaned list.
        loanedBook = new ArrayList<>();

        // Get number of books to read.
        int amount = Stream.readIntFromStream(inputStream);

        // Create books.
        for (int i = 0; i < amount; i++) {
            loanedBook.add(new Book(inputStream));
        }
    }

    // Print the loaned book list for the lender.
    public StringBuilder printBookList(){
        // Loop to print the list.
        StringBuilder bookList = new StringBuilder();

        for (Book book : loanedBook) {
            bookList.append(book);
        }

        return bookList;
    }

    public List<Book> getLoanedBook() {
        return loanedBook;
    }

    // Write lender to stream.
    public void write(OutputStream outputStream) throws IOException {
        super.write(outputStream);

        // Send amount of books.
        Stream.writeIntToServer(loanedBook.size(), outputStream);

        // Send loaned books list.
        for (Book book : loanedBook) {
            book.write(outputStream);
        }
    }

    // Print the lender details and books.
    @Override
    public String toString() {
        return super.toString() + "\n" + printBookList();
    }
}
