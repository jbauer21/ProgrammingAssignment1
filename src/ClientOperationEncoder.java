public interface ClientOperationEncoder {
        byte[] encode(ClientUDP clientUDP) throws Exception;
}
