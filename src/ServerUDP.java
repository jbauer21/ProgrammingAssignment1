//Information sent out from client.
public class ServerUDP {
    public byte TML;
    public int result;
    public byte errorCode;
    public short requestID;

    public ServerUDP(byte TML, int result, byte errorCode, short requestID){
        this.TML = TML;
        this.result = result;
        this.errorCode = errorCode;
        this.requestID = requestID;
    }
}
