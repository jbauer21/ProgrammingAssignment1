//Information sent out from client.
public class ClientUDP
{
    public byte TML;
    public byte opCode;
    public int op1;
    public int op2;
    public short requestID;
    public byte onl;
    public String operationName;

    public ClientUDP(byte TML, byte opCode, int op1, int op2, short requestID, byte onl, String operationName){
        this.TML = TML;
        this.opCode = opCode;
        this.op1 = op1;
        this.op2 = op2;
        this.requestID = requestID;
        this.onl = onl;
        this.operationName = operationName;

    }
}
