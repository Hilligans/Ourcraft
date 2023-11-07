package dev.hilligans.ourcraft.network.packet.client;

import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.Screen;
import dev.hilligans.ourcraft.container.containers.CreativeContainer;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.network.packet.server.SOpenContainer;

public class COpenScreen extends PacketBaseNew<IServerPacketHandler> {

    boolean containerScreen;
    short id;
    String screenName;

    public COpenScreen(Screen screen) {
        this();
        if(screen instanceof ContainerScreen) {
            id = (short) ((ContainerScreen) screen).container.type;
            containerScreen = true;
        }
    }

    public COpenScreen() {
        super(23);
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeShort(id);
        packetData.writeBoolean(containerScreen);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        id = packetData.readShort();
        containerScreen = packetData.readBoolean();
    }

    @Override
    public void handle(IServerPacketHandler packetHandler) {
        if(id == 2) {
            ServerPlayerData serverPlayerData = packetHandler.getServerPlayerData();
            if(serverPlayerData != null && serverPlayerData.isCreative) {
                CreativeContainer creativeContainer = new CreativeContainer(serverPlayerData.playerInventory,CreativeContainer.createInventory());
                serverPlayerData.openContainer(creativeContainer);
                packetHandler.sendPacket(new SOpenContainer(creativeContainer), ctx);
            }
        }
    }
}
