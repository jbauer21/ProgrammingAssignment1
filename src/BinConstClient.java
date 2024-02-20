//constants for ClientUDP
public interface BinConstClient {
    public static final String defaultEncoding = "UTF-16BE";
    public static final byte maxTML = 29; //Maximum message length
    public static final short maxOPCode = Short.MAX_VALUE;
    public static final int maxOperand1Length = Integer.MAX_VALUE; //maximum length for operand 1.
    public static final int maxOperand2Length = Integer.MAX_VALUE; //maximum length for operand 2.
    public static final short maxRequestID = Short.MAX_VALUE; //maximum length for operand 1.
    public static final byte maxONL  = 14; //Maximum operation name length
}
