package Hilligans.Network.Packet.Server;

import Hilligans.Client.Camera;
import Hilligans.Client.ClientPlayerData;
import Hilligans.ClientMain;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SSetGameMode extends PacketBase {

    int id;

    public SSetGameMode() {
        super(25);
    }

    public SSetGameMode(int id) {
        this();
        this.id = id;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeByte((byte)id);
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readByte();
    }

    @Override
    public void handle() {
        if(id == 0) {
            ClientMain.getClient().playerData.flying = false;
            ClientMain.getClient().playerData.creative = false;
            ClientMain.getClient().playerData.spectator = false;
        } else {
            ClientMain.getClient().playerData.creative = true;
            ClientMain.getClient().playerData.flying = true;
            ClientMain.getClient().playerData.spectator = true;
        }
    }
}
