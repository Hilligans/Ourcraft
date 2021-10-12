package dev.Hilligans.ourcraft.Network.Packet.Server;

import dev.Hilligans.ourcraft.Client.ChatWindow;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Network.PacketData;

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
    public void encode(PacketData packetData) {
        packetData.writeString(message);
    }

    @Override
    public void decode(PacketData packetData) {
        message = packetData.readString();

    }

    @Override
    public void handle() {
        ChatWindow.addMessage(message);
    }
}
