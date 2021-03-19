package Hilligans.Network.Packet.Client;

import Hilligans.Container.Containers.InventoryContainer;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Data.Other.Server.PlayerData;

public class CCloseScreen extends PacketBase {

    boolean newScreen = false;

    public CCloseScreen() {
        super(20);
    }

    public CCloseScreen(boolean newScreen) {
        this();
        this.newScreen = newScreen;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeBoolean(newScreen);
    }

    @Override
    public void decode(PacketData packetData) {
        newScreen = packetData.readBoolean();
    }

    @Override
    public void handle() {
        if(!newScreen) {
            PlayerData playerData = ServerNetworkHandler.getPlayerData(ctx);
            if (playerData != null) {
                playerData.openContainer(new InventoryContainer(playerData.playerInventory));
            }
        }
    }
}
