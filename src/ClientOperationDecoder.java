import Client.ClientPackage;

import java.io.*;   // for InputStream and IOException
import java.net.*;  // for DatagramPacket

public interface ClientOperationDecoder {
    ClientPackage decode(InputStream source) throws IOException;
    ClientPackage decode(DatagramPacket packet) throws IOException;
}
