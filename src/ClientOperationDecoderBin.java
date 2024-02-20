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

    public ClientUDP decode(InputStream wire) throws IOException {
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

        // Build class and return
        return new ClientUDP(TML, opCode, op1, op2, requestID,
            onl, operationName);
    }

    public ClientUDP decode(DatagramPacket p) throws IOException {
        ByteArrayInputStream payload =
                new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
        return decode(payload);
    }
}
