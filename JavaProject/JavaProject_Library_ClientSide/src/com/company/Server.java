package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

    public static final int PORT = 1996;
    public static final String HOST = "127.0.0.1";

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


    // Connection to server.
    private static void connect(ConnectionListener listener){
        Socket socket = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;

        // Connect to server.
        try {
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            listener.onConnect(inputStream, outputStream);
        } catch (UnknownHostException e) {
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

    // Login to user
    public static void login(String name, String password, final ResultSuccessListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
               try {
                   Stream.writeIntToServer(LOGIN, outputStream);

                   // Send username.
                   Stream.writeStringToServer(name, outputStream);

                   // Send password.
                   Stream.writeStringToServer(password, outputStream);

                   // Get feedback from server
                   int response = Stream.readIntFromStream(inputStream);
                   if (listener != null){
                       if (response == OKAY)
                           listener.onResult(true);
                       else
                           listener.onResult(false);
                   }

               }catch (IOException ex){
                   if (listener != null){
                       listener.onResult(false);
                       throw ex;
                   }
               }
            }
        });
    }

    public static void addLibrarian(String name, String password, final ResultSuccessListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(ADD_LIBRARIAN, outputStream);

                    // Send user name and password.
                    Stream.writeStringToServer(name, outputStream);
                    Stream.writeStringToServer(password, outputStream);

                    // Get feedback from server.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null){
                        if (response == OKAY)
                            listener.onResult(true);
                        else
                            listener.onResult(false);
                    }
                }catch (IOException ex){
                    if (listener != null){
                        listener.onResult(false);
                        throw ex;
                    }
                }
            }
        });
    }

    public static void addABook(Book book, final ResultSuccessListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(ADD_BOOK, outputStream);

                    // Send book.
                    book.write(outputStream);

                    // Get feedback from server.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null){
                        if (response == OKAY)
                            listener.onResult(true);
                        else
                            listener.onResult(false);
                    }
                }catch (IOException ex){
                    if (listener != null){
                        listener.onResult(false);
                        throw ex;
                    }
                }
            }
        });
    }

    public static void deleteABook(Book book, final ResultListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(DELETE_BOOK, outputStream);

                    // Send book.
                    book.write(outputStream);

                    // Get feedback.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null) {
                        if (response == OKAY)
                            listener.onResult(OKAY);
                        else if (response == NULL)
                            // Book loaned, Can't delete.
                            listener.onResult(NULL);
                        else
                            // Book does'nt exist.
                            listener.onResult(FAILURE);
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public static void searchABook(Book book, final BookListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(SEARCH_BOOK, outputStream);

                    // Send a book.
                    book.write(outputStream);

                    // Get if exist.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null){
                        if (response == OKAY) {
                            Book book = new Book(inputStream);
                            listener.onBook(book);
                        }
                        else
                            // Not exist.
                            listener.onBook(null);
                    }
                }catch (IOException ex) {
                    throw ex;
                }
            }
        });
    }

    public static void searchGenre(String genre, final BookListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(SEARCH_GENRE, outputStream);

                    // Send genre.
                    Stream.writeStringToServer(genre, outputStream);

                    // Get amount.
                    int amountOfBooks = Stream.readIntFromStream(inputStream);

                    // If there is no books on list.
                    if (amountOfBooks == 0)
                        listener.onBook(null);

                    for (int i = 0; i < amountOfBooks; i++) {
                        Book book = new Book(inputStream);
                        listener.onBook(book);
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public static void printFullList(final BookListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(PRINT_FULL_LIST, outputStream);

                    // Print all books.
                    int amountOfBooks = Stream.readIntFromStream(inputStream);

                    // If there is no books on list.
                    if (listener != null) {
                        if (amountOfBooks == 0)
                            listener.onBook(null);

                        for (int i = 0; i < amountOfBooks; i++) {
                            Book book = new Book(inputStream);
                            listener.onBook(book);
                        }
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public static void searchLender(Lender lender, final LenderListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(SEARCH_LENDER, outputStream);

                    // Send a lender.
                    lender.write(outputStream);

                    // Get if exist.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null){
                        if (response == OKAY){
                            Lender lender = new Lender(inputStream);
                            listener.onLender(lender);
                        }
                        // Not exist.
                        else
                            listener.onLender(null);
                    }
                }catch (IOException ex) {
                    throw ex;
                }
            }
        });
    }

    public static void addLender(Lender lender, final ResultSuccessListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(ADD_LENDER, outputStream);

                    // Send lender.
                    lender.write(outputStream);

                    // Get feedback from server.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null){
                        if (response== OKAY)
                            listener.onResult(true);
                        else
                            listener.onResult(false);
                    }
                }catch (IOException ex){
                    if (listener != null){
                        listener.onResult(false);
                        throw ex;
                    }
                }
            }
        });
    }

    public static void deleteLender(Lender lender, final ResultListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(DELETE_LENDER, outputStream);

                    // Send lender.
                    lender.write(outputStream);

                    // Get feedback.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null) {
                        if (response == OKAY)
                            listener.onResult(OKAY);
                        else if (response == NULL)
                            // Lender has books. Can't delete.
                            listener.onResult(NULL);
                        else
                            // Lender does'nt exist.
                            listener.onResult(FAILURE);
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public static void lendBook(Book book, Lender lender, final ResultListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(LEND_BOOK, outputStream);

                    // Send lender.
                    lender.write(outputStream);

                    // Send book.
                    book.write(outputStream);

                    // Get feedback.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null) {
                        if (response == OKAY)
                            // All good.
                            listener.onResult(OKAY);
                        else if (response == NULL)
                            // Book already loaned.
                            listener.onResult(NULL);
                        else if (response == B_L_NULL)
                            // Book does'nt exist.
                            listener.onResult(B_L_NULL);
                        else
                            // Lender does'nt exist.
                            listener.onResult(FAILURE);
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public static void returnBook(Book book, Lender lender, final ResultListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(RETURN_BOOK, outputStream);

                    // Send lender.
                    lender.write(outputStream);

                    // Send book.
                    book.write(outputStream);

                    // Get feedback.
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null) {
                        if (response == OKAY)
                            // All good.
                            listener.onResult(OKAY);
                        else if (response == NULL)
                            // Book loan to someone else.
                            listener.onResult(NULL);
                        else if (response == B_L_NULL)
                            // Book does'nt exist.
                            listener.onResult(B_L_NULL);
                        else
                            // Lender does'nt exist.
                            listener.onResult(FAILURE);
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public static void printAllLenders(final LenderListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(PRINT_LENDERS, outputStream);

                    // Print all lenders.
                    int amountOfLenders = Stream.readIntFromStream(inputStream);

                    // If there is no lenders on list.
                    if (listener != null) {
                        if (amountOfLenders == 0)
                            listener.onLender(null);

                        for (int i = 0; i < amountOfLenders; i++) {
                            Lender lender = new Lender(inputStream);
                            listener.onLender(lender);
                        }
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public static void saveData(final ResultSuccessListener listener){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                try {
                    Stream.writeIntToServer(SAVE_DATA, outputStream);

                    // Get feedback from server
                    int response = Stream.readIntFromStream(inputStream);
                    if (listener != null){
                        if (response == OKAY)
                            listener.onResult(true);
                        else
                            listener.onResult(false);
                    }
                }catch (IOException ex){
                    throw ex;
                }
            }
        });
    }

    public interface ConnectionListener{
        void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException;
    }

    public interface ResultSuccessListener{
        void onResult(Boolean success);
    }

    public interface ResultListener{
        void onResult(int result);
    }

    public interface BookListener{
        void onBook(Book book);
    }

    public interface LenderListener{
        void onLender(Lender lender);
    }
}
