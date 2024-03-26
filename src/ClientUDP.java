import Client.ClientOperationEncoder;
import Client.ClientOperationEncoderBin;
import Client.ClientPackage;
import Server.ServerOperationDecoder;
import Server.ServerOperationDecoderBin;
import Server.ServerPackage;

import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;

public class ClientUDP {

    // Arguments
    // -> IP Address
    // -> Port Number [10017]
    public static void main(String args[]) throws Exception {

        // Test for the correct number of arguments
        if (args.length != 2 && args.length != 3)
            throw new IllegalArgumentException("Parameter(s): <Destination>" +
                    " <Port> [<encoding]");


        InetAddress destAddr = InetAddress.getByName(args[0]);  // Destination address
        int destPort = Integer.parseInt(args[1]);               // Destination port
        System.out.println(destAddr);
        // Create a sample ClientUDP


        // COMMENT INFO BELOW
        Scanner scan = new Scanner(System.in);
        short requestID = 0;
        while(true) {
            System.out.println("----- CLIENT INPUT -----");
            System.out.println("- VALID OPERATIONS\n > 0 = addition\n > 1 = subtraction\n > 2 = or\n > 3 = and\n > 4 = division\n > 5 = multiplication" +
                    "\n\n[USE CTRL+C AT ANY TIME TO QUIT]");
            System.out.println("Please enter your desired operation (0-5):");
            byte opCode = scan.nextByte();
            if (opCode < 0 || opCode > 5)
                throw new IOException("invalid length of opcode!");
            String operationName = "";
            byte onl;
            switch (opCode) {
                case 5:
                    operationName = "addition";
                    break;
                case 4:
                    operationName = "subtraction";
                    break;
                case 2:
                    operationName = "or";
                    break;
                case 3:
                    operationName = "and";
                    break;
                case 1:
                    operationName = "division";
                    break;
                case 0:
                    operationName = "multiplication";
                    break;
            }
            onl = (byte) (operationName.length() * 2);
            System.out.println("Please enter your first operand:");
            int op1 = scan.nextInt();
            System.out.println("Please enter your second operand:");
            int op2 = scan.nextInt();
            byte TML = (byte) (13 + onl / 2);

            ClientPackage newClient = new ClientPackage(TML, opCode, op1,
                    op2, requestID, onl, operationName);
            System.out.println(newClient);

            DatagramSocket sock = new DatagramSocket(); // UDP socket for sending

            // Use the encoding scheme given on the command line (args[2])
            ClientOperationEncoder cEncoder = (args.length == 3 ?
                    new ClientOperationEncoderBin(args[2]) :
                    new ClientOperationEncoderBin());

            // Use Encoding Scheme (Client Encoder)
            byte[] codedFriend = cEncoder.encode(newClient);

            DatagramPacket message = new DatagramPacket(codedFriend, codedFriend.length,
                    destAddr, destPort);
            sock.send(message);
            long sTime = System.nanoTime();
            sock.close();



            // Wait for a response
            sock = null;
            while(sock == null){
                try{
                    sock = new DatagramSocket(destPort);
                }catch (Exception e) {}
            }
            DatagramPacket sPacket = new DatagramPacket(new byte[1024], 1024);

            System.out.println("Waiting for Server...\n");
            sock.receive(sPacket);
            long eTime = System.nanoTime();
            // -> Print response time
            System.out.println("----- RESPONSE TIME -----\n" + (eTime - sTime));

            // - Split into Server and Client Decoders
            ServerOperationDecoder sDecoder = (args.length == 2 ?
                    new ServerOperationDecoderBin(args[1]) :
                    new ServerOperationDecoderBin());

            ServerPackage recPack = sDecoder.decode(sPacket);

            System.out.println("----- RECEIVED -----\n" + recPack);

            sock.close();
            requestID += 1;
        }
    }
}
