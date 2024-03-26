package Client;

import Client.ClientPackage;

public interface ClientOperationEncoder {
        byte[] encode(ClientPackage clientUDP) throws Exception;
}
