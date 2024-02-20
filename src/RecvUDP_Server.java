import java.net.*;  // for DatagramSocket and DatagramPacket
import java.io.*;   // for IOException

public class RecvUDP_Server {

  public static void main(String[] args) throws Exception {

      // Test for correct # of args
      if (args.length != 1 && args.length != 2)
	    throw new IllegalArgumentException("Parameter(s): <Port> [<encoding>]");

      // Receiving Port
      int port = Integer.parseInt(args[0]);

      // Run the server - Keep running until CTRL+C
      while(true) {
          // UDP socket for receiving
          DatagramSocket sock = new DatagramSocket(port);
          DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

          // Wait to receive a package
          System.out.println("Waiting for Client...");
          sock.receive(packet);

          // Receive binary-encoded packet
          // - Split into Server and Client Decoders
          ClientOperationDecoder cDecoder = (args.length == 2 ?
                  new ClientOperationDecoderBin(args[1]) :
                  new ClientOperationDecoderBin());


          // Mark the received package
          ClientUDP rClientPackage = cDecoder.decode(packet);

          // Pull the Received data
          System.out.println("Received Binary-Encoded Client UDP");
          System.out.println(rClientPackage);

          // Operands
          int op1 = rClientPackage.op1;
          int op2 = rClientPackage.op2;

          // Stored values
          int result = 0;
          byte errorCode = 0;
          short requestID = rClientPackage.requestID;

          // Run the operation
          switch (rClientPackage.opCode){
              // Multiplication
              case 0:
                  result = op1 * op2;
                  break;
              // Division
              case 1:
                  // Check if operand 2 is 0
                  if(op2 == 0){
                      errorCode = (byte) 127;
                      result = 0;
                      break;
                  }
                  result = op1 / op2;
                  break;
              // Bitwise Or
              case 2:
                  break;
              // Bitwise And
              case 3:
                  break;
              // Subtraction
              case 4:
                  result = op1 - op2;
                  break;
              // Addition
              case 5:
                  result = op1 + op2;
                  break;
          }

          // Send result back to client
          // -> Construct a ServerUDP
          ServerUDP sPackage = new ServerUDP((byte) 8, result, errorCode, requestID);
          // -> Throw a quick lil debug message
          System.out.println("----- SENDING -----\n" +sPackage);


          sock.close();


          // Establish new connection
          System.out.println("----- CONNECTING -----\n" + packet.getAddress() + " : " + port);
          sock = new DatagramSocket();
          // -> Send out encoded result
          // --> Encode
          ServerOperationEncoder sEncoder = (args.length == 2 ?
                  new ServerOperationEncoderBin(args[1]) :
                  new ServerOperationEncoderBin());
          byte[] codedServerRes = sEncoder.encode(sPackage);
          // --> Pack
          DatagramPacket resPacket =new DatagramPacket(codedServerRes, codedServerRes.length,
                  packet.getAddress(), port);
          // --> Send
          sock.send(resPacket);

          // --> Close socket
          sock.close();

          System.out.println("!! SENT PACKAGE !!");
      }
  }
}