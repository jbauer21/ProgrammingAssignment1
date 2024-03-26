package Server;

public interface ServerOperationEncoder {
    byte[] encode(ServerPackage serverUDP) throws Exception;
}
