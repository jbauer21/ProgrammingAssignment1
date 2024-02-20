import java.io.*;   // for InputStream and IOException
import java.net.*;  // for DatagramPacket
public interface ServerOperationDecoder {
    ServerUDP decode(InputStream source) throws IOException;
    ServerUDP decode(DatagramPacket packet) throws IOException;
}
