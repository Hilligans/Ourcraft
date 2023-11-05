package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Container.Containers.InventoryContainer;
import dev.hilligans.ourcraft.Network.*;
import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;

public class CCloseScreen extends PacketBaseNew<IServerPacketHandler> {

    boolean newScreen = false;

    public CCloseScreen() {
        super(20);
    }

    public CCloseScreen(boolean newScreen) {
        this();
        this.newScreen = newScreen;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeBoolean(newScreen);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        newScreen = packetData.readBoolean();
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        if(!newScreen) {
            ServerPlayerData serverPlayerData = serverPacketHandler.getServerPlayerData();
            if (serverPlayerData != null) {
                serverPlayerData.openContainer(new InventoryContainer(serverPlayerData.playerInventory));
            }
        }
    }
}
