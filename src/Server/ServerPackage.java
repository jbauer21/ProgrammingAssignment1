package Server;

//Information sent out from client.
public class ServerPackage {
    public byte TML;
    public int result;
    public byte errorCode;
    public short requestID;

    public ServerPackage(byte TML, int result, byte errorCode, short requestID){
        this.TML = TML;
        this.result = result;
        this.errorCode = errorCode;
        this.requestID = requestID;
    }

    public String toString(){
        final String EOLN = java.lang.System.getProperty("line.separator");
        String value = "TML: " + TML + EOLN +
                        "RESULT: " + result + EOLN +
                        "ERROR CODE: " + errorCode + EOLN +
                        "REQUEST ID: " + requestID + EOLN;
        return value;
    }
}
