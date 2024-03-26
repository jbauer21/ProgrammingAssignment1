import Client.BinConstClient;
import Client.ClientPackage;

import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class ClientOperationDecoderBin implements ClientOperationDecoder, BinConstClient {

    private String encoding;  // Character encoding

    public ClientOperationDecoderBin() {
        encoding = defaultEncoding;
    }

    public ClientOperationDecoderBin(String encoding) {
        this.encoding = encoding;
    }

    public ClientPackage decode(InputStream wire) throws IOException {
        DataInputStream src = new DataInputStream(wire);

        // Read source data
        byte TML        = src.readByte();
        byte opCode     = src.readByte();
        int op1         = src.readInt();
        int op2         = src.readInt();
        short requestID = src.readShort();

        // Pull operation name length
        byte onl = src.readByte();

        // Check if operation length name is not -1
        if(onl == -1)
            throw new EOFException();

        // Pull operation name
        byte[] stringBuf = new byte[onl];
        src.readFully(stringBuf);
        String operationName = new String(stringBuf, encoding);


        // Print Bytes
        System.out.println("\n----- BYTES -----");
        // - Bytes
        System.out.print(" | " + Integer.toHexString(TML));
        System.out.print(" | " + Integer.toHexString(opCode));
        // - Integer
        // -- OP1
        byte[] op1Byte = IntToByteArray(op1);
        for(byte b : op1Byte)
            System.out.print(" | " + Integer.toHexString(b));
        // -- OP2
        byte[] op2Byte = IntToByteArray(op2);
        for(byte b : op2Byte)
            System.out.print(" | " + Integer.toHexString(b));
        // - Short
        // -- Request ID
        byte[] reqIDByte = ShortToByteArray(requestID);
        for(byte b : reqIDByte)
            System.out.print(" | " + Integer.toHexString(b));
        // - ONL
        System.out.print(" | " + Integer.toHexString(onl));
        // - String
        byte[] stringByte = operationName.getBytes();
        for(int i = 0; i < stringByte.length; i++) {
            System.out.print(" | 0");
            System.out.print(" | " + Integer.toHexString(stringByte[i]));
        }
        System.out.print(" | \n\n");


        // Build class and return
        return new ClientPackage(TML, opCode, op1, op2, requestID,
            onl, operationName);
    }

    public ClientPackage decode(DatagramPacket p) throws IOException {
        ByteArrayInputStream payload =
                new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
        return decode(payload);
    }

    byte[] IntToByteArray( int data ) {
        byte[] result = new byte[4];
        result[0] = (byte) ((data & 0xFF000000) >> 24);
        result[1] = (byte) ((data & 0x00FF0000) >> 16);
        result[2] = (byte) ((data & 0x0000FF00) >> 8);
        result[3] = (byte) ((data & 0x000000FF) >> 0);
        return result;
    }
    byte[] ShortToByteArray( int data ) {
        byte[] result = new byte[2];
        result[0] = (byte) ((data & 0xFF00) >> 8);
        result[1] = (byte) ((data & 0x00FF) >> 0);
        return result;
    }
}
