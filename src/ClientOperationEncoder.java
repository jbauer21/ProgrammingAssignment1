public interface ClientOperationEncoder {
        byte[] encode(ClientPackage clientUDP) throws Exception;
}
