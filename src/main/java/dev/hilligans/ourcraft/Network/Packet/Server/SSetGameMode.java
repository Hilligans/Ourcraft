package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.*;

public class SSetGameMode extends PacketBaseNew<IClientPacketHandler> {

    int id;

    public SSetGameMode() {
        super(25);
    }

    public SSetGameMode(int id) {
        this();
        this.id = id;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeByte((byte)id);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        id = packetData.readByte();
    }

    @Override
    public void handle(IClientPacketHandler clientPacketHandler) {
        Client client = clientPacketHandler.getClient();
        if(id == 0) {
            client.playerData.flying = false;
            client.playerData.creative = false;
            client.playerData.spectator = false;
        } else {
            client.playerData.creative = true;
            client.playerData.flying = true;
            client.playerData.spectator = true;
        }
    }
}
