import Client.ClientOperationDecoder;
import Client.ClientOperationDecoderBin;
import Client.ClientPackage;
import Server.*;

import java.io.OutputStream;
import java.net.*;  // for DatagramSocket and DatagramPacket


public class ServerTCP {

  public static void main(String[] args) throws Exception {

      if (args.length != 1)  // Test for correct # of args
          throw new IllegalArgumentException("Parameter(s): <Port>");

      // Receiving Port
      int port = Integer.parseInt(args[0]);

      // Run the server - Keep running until CTRL+C
      while(true) {
          // TCP socket for receiving
          System.out.println("Establishing server on port " + port);
          ServerSocket sock = null;
          boolean errorThrown = false;
          int attemtps = 0;
          while(sock == null){
              try{
                  sock = new ServerSocket(port);
              }catch (Exception e) {
                  if(!errorThrown) {
                      System.out.println("FAILED TO ESTABLISH ON PORT " + port + " THROWING EXECPTION " + e +
                              "\nAttempts: ");
                      errorThrown = true;
                  }
              }
          }
          System.out.println("Server established on port " + port);

          // Wait to receive a package
          System.out.println("Waiting for Client...\n[USE CTRL+C AT ANY TIME TO QUIT]");
          Socket packet = sock.accept();


          // Receive binary-encoded packet
          // - Split into Server and Client Decoders
          ClientOperationDecoder cDecoder = new ClientOperationDecoderBin();


          // Mark the received package
          ClientPackage rClientPackage = cDecoder.decode(packet.getInputStream());
          InetAddress resAddr = packet.getLocalAddress();

          // Pull the Received data
          System.out.println("Received Binary-Encoded Client UDP");
          System.out.println(rClientPackage);

          sock.close();
          packet.close();

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
              case 5:
                  result = op1 * op2;
                  break;
              // Division
              case 4:
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
                  result = op1 | op2;
                  break;
              // Bitwise And
              case 3:
                  result = op1 & op2;
                  break;
              // Subtraction
              case 1:
                  result = op1 - op2;
                  break;
              // Addition
              case 0:
                  result = op1 + op2;
                  break;
          }

          // Send result back to client - NON FUNCTIONAL
          // -> Construct a ServerUDP
          ServerPackage sPackage = new ServerPackage((byte) 8, result, errorCode, requestID);
          // -> Throw a quick lil debug message
          System.out.println("----- SENDING -----\n" +sPackage);


          // Establish new connection
          System.out.println("----- CONNECTING -----\n" + resAddr + " : " + port);
          Socket socket = new Socket(resAddr, port);
          // -> Send out encoded result
          // --> Encode
          ServerOperationEncoder sEncoder = new ServerOperationEncoderBin();
          OutputStream out = socket.getOutputStream();

          // --> Send
          out.write(sEncoder.encode(sPackage));

          // --> Close socket
          out.close();
          socket.close();
          sock.close();

          System.out.println("!! SENT PACKAGE !!");
      }
  }
}