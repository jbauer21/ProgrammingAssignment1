package Server;

import java.io.*;   // for InputStream and IOException
import java.net.*;  // for DatagramPacket
public interface ServerOperationDecoder {
    ServerPackage decode(InputStream source) throws IOException;
    ServerPackage decode(DatagramPacket packet) throws IOException;
}
