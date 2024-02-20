import java.io.*;  // for ByteArrayOutputStream and DataOutputStream

public class ServerOperationEncoderBin implements ServerOperationEncoder, BinConstServer {

    private String encoding;  // Character encoding

    public ServerOperationEncoderBin() {
        encoding = defaultEncoding;
    }

    public ServerOperationEncoderBin(String encoding) {
        this.encoding = encoding;
    }
    public byte[] encode(ServerUDP serverUDP) throws Exception {

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);

        if (serverUDP.TML > maxTML) //check to see if total message length is too big
            throw new IOException("total message length exceeds encoded length limit.");
        out.writeByte(serverUDP.TML); //write total message length
        out.writeInt(serverUDP.result); //write result

        out.writeByte(serverUDP.errorCode); //write error code
        out.writeShort(serverUDP.requestID);//write request ID
        out.flush();
        return buf.toByteArray();
    }
}



