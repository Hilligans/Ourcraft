package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.Client.ChatWindow;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Network.*;

public class SChatMessage extends PacketBaseNew<IClientPacketHandler> {

    String message;

    public SChatMessage() {
        super(12);
    }

    public SChatMessage(String message) {
        this();
        this.message = message;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeUTF16(message);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        message = packetData.readUTF16();

    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        Client client = clientPacketHandler.getClient();
        client.chatMessages.addMessage(message);
    }
}
