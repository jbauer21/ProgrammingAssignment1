import java.net.*;  // for DatagramSocket and DatagramPacket
import java.io.*;   // for IOException

public class RecvUDP {

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

      /*FriendDecoder decoder = (args.length == 2 ?
				  new FriendDecoderBin(args[1]) :
				  new FriendDecoderBin() );*/


      //Friend receivedFriend = decoder.decode(packet);

      System.out.println("Received Binary-Encoded Friend");
      //System.out.println(receivedFriend);
      
      sock.close();
  }
}
