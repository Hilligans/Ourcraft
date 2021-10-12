package dev.Hilligans.Ourcraft.Network.Packet.Client;

import dev.Hilligans.Ourcraft.Container.Containers.InventoryContainer;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;
import dev.Hilligans.Ourcraft.Network.ServerNetworkHandler;
import dev.Hilligans.Ourcraft.Data.Other.Server.ServerPlayerData;

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
            ServerPlayerData serverPlayerData = ServerNetworkHandler.getPlayerData(ctx);
            if (serverPlayerData != null) {
                serverPlayerData.openContainer(new InventoryContainer(serverPlayerData.playerInventory));
            }
        }
    }
}
