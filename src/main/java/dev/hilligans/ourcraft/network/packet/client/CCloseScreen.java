package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.container.containers.InventoryContainer;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;

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
