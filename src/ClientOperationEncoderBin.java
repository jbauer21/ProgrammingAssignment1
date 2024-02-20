import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

    public class ClientOperationEncoderBin implements ClientOperationEncoder, BinConstClient {

        private String encoding;  // Character encoding

        public ClientOperationEncoderBin() {
            encoding = defaultEncoding;
        }

        public ClientOperationEncoderBin(String encoding) {
            this.encoding = encoding;
        }
        public byte[] encode(ClientPackage clientUDP) throws Exception {

            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(buf);

            if (clientUDP.TML > maxTML) //check to see if total message length is too big
                throw new IOException("total message length exceeds encoded length limit.");

            out.writeByte(clientUDP.TML); //write total message length
            out.writeByte(clientUDP.opCode); //write operation code
            out.writeInt(clientUDP.op1); //write operation 1
            out.writeInt(clientUDP.op2); //write operation 2
            out.writeShort(clientUDP.requestID); //write request id

            System.out.println("ONL: " + clientUDP.onl);

            if (clientUDP.onl > maxONL) //check to see if operation name length is too big.
                throw new IOException("operation name length exceeds encoded length limit.");

            out.writeByte(clientUDP.onl);



            byte[] encodedOperationMessage = clientUDP.operationName.getBytes(encoding);
            out.write(encodedOperationMessage);

            out.flush();
            byte[] totalArray = buf.toByteArray();
            /*
            for(byte b : totalArray){
                System.out.println(b);
            }
            */
            return buf.toByteArray();
        }
    }


