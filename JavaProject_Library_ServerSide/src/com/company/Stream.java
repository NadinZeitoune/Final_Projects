package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Stream {

    // Write method.
    public static void writeStringToServer(String string, OutputStream outputStream) throws IOException{
        // Send length and string.
        byte[] bytes = string.getBytes();
        Stream.writeIntToServer(bytes.length, outputStream);
        outputStream.write(bytes);
    }

    public static void writeIntToServer(int x, OutputStream outputStream) throws IOException{
        byte[] xBytes = new byte[4];
        ByteBuffer.wrap(xBytes).putInt(x);
        outputStream.write(xBytes);
    }

    public static void writeLongToServer(Long x, OutputStream outputStream) throws IOException{
        byte[] xBytes = new byte[8];
        ByteBuffer.wrap(xBytes).putLong(x);
        outputStream.write(xBytes);
    }

    // Read methods.
    public static String readStringFromStream(InputStream inputStream) throws IOException {
        int stringLength = readIntFromStream(inputStream);
        byte[] string = new byte[stringLength];
        int actuallyRead = inputStream.read(string);
        if (actuallyRead != stringLength)
            throw new IOException("Expected " + stringLength + " bytes but received " + actuallyRead);
        return new String(string);
    }

    public static int readIntFromStream(InputStream inputStream) throws IOException{
        byte[] lBytes = new byte[4];
        int actuallyRead = inputStream.read(lBytes);
        if(actuallyRead != 4)
            throw new RuntimeException("Expected 4 bytes but received " + actuallyRead);
        return ByteBuffer.wrap(lBytes).getInt();
    }

    public static Long readLongFromStream(InputStream inputStream) throws IOException{
        byte[] lBytes = new byte[8];
        int actuallyRead = inputStream.read(lBytes);
        if(actuallyRead != 8)
            throw new RuntimeException("Expected 8 bytes but received " + actuallyRead);
        return ByteBuffer.wrap(lBytes).getLong();
    }
}
