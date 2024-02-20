import java.net.*;  // for DatagramSocket and DatagramPacket
import java.io.*;   // for IOException

public class RecvUDP_Server {

  public static void main(String[] args) throws Exception {

      // Test for correct # of args
      if (args.length != 1 && args.length != 2)
	    throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");

      // Receiving Port
      int port = Integer.parseInt(args[0]);

      // UDP socket for receiving
      DatagramSocket sock = new DatagramSocket(port);
      DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
      // Wait to receive a package
      sock.receive(packet);

      // Receive binary-encoded packet
      // - Split into Server and Client Decoders

      ClientOperationDecoder cDecoder = (args.length == 2 ?
				  new ClientOperationDecoderBin(args[1]) :
				  new ClientOperationDecoderBin() );


      // Mark the received package
      ClientUDP rClientPackage = cDecoder.decode(packet);

      System.out.println("Received Binary-Encoded Client UDP");
      System.out.println(rClientPackage);

      sock.close();
  }
}