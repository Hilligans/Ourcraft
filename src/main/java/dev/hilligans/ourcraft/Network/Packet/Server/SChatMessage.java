package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.Client.ChatWindow;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SChatMessage extends PacketBase {

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
    public void handle() {
        ChatWindow.addMessage(message);
    }
}
