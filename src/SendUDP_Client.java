import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class SendUDP_Client {

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

        // Create a sample ClientUDP
        ClientUDP sampleClient = new ClientUDP((byte) 1, (byte) 2, (byte) 3,
                (byte) 4, (short) 5, (byte) 8, "test");
        // -> Loop for user input (NOT IMPLEMENTED)

        System.out.println(sampleClient);

        DatagramSocket sock = new DatagramSocket(); // UDP socket for sending

        // Use the encoding scheme given on the command line (args[2])
        ClientOperationEncoder cEncoder = (args.length == 3 ?
                new ClientOperationEncoderBin(args[2]) :
                new ClientOperationEncoderBin() );

        // Use Encoding Scheme (Client Encoder)
        byte[] codedFriend = cEncoder.encode(sampleClient);

        DatagramPacket message = new DatagramPacket(codedFriend, codedFriend.length,
                                  destAddr, destPort);
        sock.send(message);

        sock.close();
    }
}
