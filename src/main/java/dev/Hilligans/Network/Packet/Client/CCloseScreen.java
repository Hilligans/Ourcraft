package dev.Hilligans.Network.Packet.Client;

import dev.Hilligans.Container.Containers.InventoryContainer;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;
import dev.Hilligans.Network.ServerNetworkHandler;
import dev.Hilligans.Data.Other.Server.ServerPlayerData;

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