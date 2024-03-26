import Client.ClientOperationEncoder;
import Client.ClientOperationEncoderBin;
import Client.ClientPackage;
import Server.ServerOperationDecoderBin;

import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner;

public class ClientTCP {

    // Arguments
    // -> IP Address
    // -> Port Number [10017]
    public static void main(String args[]) throws Exception {

        if (args.length != 2)  // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Destination> <Port>");


        // EST TCP CONNECTION
        InetAddress destAddr = InetAddress.getByName(args[0]);  // Destination address
        int destPort = Integer.parseInt(args[1]);               // Destination port
        System.out.println("Establishing connection with " + destAddr);
        // EST Socket - TCP


        // COMMENT INFO BELOW
        Scanner scan = new Scanner(System.in);
        short requestID = 0;
        while(true) {
            Socket sock = new Socket(destAddr, destPort);
            System.out.println("Connection Established");

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
                case 0:
                    operationName = "addition";
                    break;
                case 1:
                    operationName = "subtraction";
                    break;
                case 2:
                    operationName = "or";
                    break;
                case 3:
                    operationName = "and";
                    break;
                case 4:
                    operationName = "division";
                    break;
                case 5:
                    operationName = "multiplication";
                    break;
            }
            onl = (byte) (operationName.length() * 2);
            System.out.println("Please enter your first operand:");
            int op1 = scan.nextInt();
            System.out.println("Please enter your second operand:");
            int op2 = scan.nextInt();
            byte TML = (byte) (13 + onl / 2);

            // Create the client package
            ClientPackage newClient = new ClientPackage(TML, opCode, op1,
                    op2, requestID, onl, operationName);
            System.out.println(newClient);

            // -> OLD EST DG SOCK - UDP

            // Use the encoding scheme given on the command line (args[2])
            //Client.ClientOperationEncoder cEncoder = (args.length == 3 ?
            //        new Client.ClientOperationEncoderBin(args[2]) :
            //        new Client.ClientOperationEncoderBin());

            // Use Encoding Scheme (Client Encoder)
            //byte[] codedFriend = cEncoder.encode(newClient); //UDP
            // Create New Encoder BIN - TCP
            ClientOperationEncoder coe = new ClientOperationEncoderBin();

            // -> OLD DG PACKET - UDP

            // -> OLD DG SEND - UDP

            // Create an output stream and write
            OutputStream out = sock.getOutputStream();
            out.write(coe.encode(newClient));
            // Mark send time and close stream
            long sTime = System.nanoTime();
            out.close();
            //sock.close();



            // Wait for a response - NOT FUNCTIONAL
            ServerSocket servSock = null;
            while(servSock == null){
                try{
                    servSock = new ServerSocket(destPort);
                }catch (Exception e) {}
            }
            Socket resSock = servSock.accept();

            System.out.println("Waiting for Server...\n");
            long eTime = System.nanoTime();
            // -> Print response time
            System.out.println("----- RESPONSE TIME -----\n" + (eTime - sTime));

            // - Split into Server and Client Decoders
            Server.ServerOperationDecoder sDecoder = new ServerOperationDecoderBin();

            Server.ServerPackage recPack = sDecoder.decode(resSock.getInputStream());

            System.out.println("----- RECEIVED -----\n" + recPack);

            resSock.close();
            servSock.close();
            requestID += 1;
        }
    }
}
