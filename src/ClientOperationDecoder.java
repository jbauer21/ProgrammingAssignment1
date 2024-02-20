import java.io.*;   // for InputStream and IOException
import java.net.*;  // for DatagramPacket

public interface ClientOperationDecoder {
    ClientUDP decode(InputStream source) throws IOException;
    ClientUDP decode(DatagramPacket packet) throws IOException;
}
