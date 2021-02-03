package Hilligans.Network.Packet.Client;

import Hilligans.Network.Packet.Server.SChatMessage;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;

public class CSendMessage extends PacketBase {

    String message;

    public CSendMessage() {
        super(11);
    }

    public CSendMessage(String message) {
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
        String name = ServerNetworkHandler.mappedName.get(ctx.channel().id());
        System.out.println(name + ": " + message);
        ServerNetworkHandler.sendPacket(new SChatMessage(name + ": " + message));
    }
}
