import java.io.*;  // for ByteArrayInputStream
import java.net.*; // for DatagramPacket

public class ServerOperationDecoderBin implements ServerOperationDecoder, BinConstServer {

    private String encoding;  // Character encoding

    public ServerOperationDecoderBin() {
        encoding = defaultEncoding;
    }

    public ServerOperationDecoderBin(String encoding) {
        this.encoding = encoding;
    }

    public ServerPackage decode(InputStream wire) throws IOException {
        boolean single, rich, female;
        DataInputStream src = new DataInputStream(wire);
        byte TML = src.readByte();
        int result = src.readInt();
        byte errorCode = src.readByte();
        short requestID = src.readShort();


        return new ServerPackage(TML, result, errorCode, requestID);
    }

    public ServerPackage decode(DatagramPacket p) throws IOException {
        ByteArrayInputStream payload =
                new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
        return decode(payload);
    }
}

