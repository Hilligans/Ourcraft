package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.hilligans.ourcraft.Client.Rendering.Screen;
import dev.hilligans.ourcraft.Container.Containers.CreativeContainer;
import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.Packet.Server.SOpenContainer;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;

public class COpenScreen extends PacketBase {

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
    public void handle() {
        if(id == 2) {
            ServerPlayerData serverPlayerData = ServerNetworkHandler.getPlayerData(ctx);
            if(serverPlayerData != null && serverPlayerData.isCreative) {
                CreativeContainer creativeContainer = new CreativeContainer(serverPlayerData.playerInventory,CreativeContainer.createInventory());
                serverPlayerData.openContainer(creativeContainer);
                ServerNetworkHandler.sendPacket(new SOpenContainer(creativeContainer), ctx);
            }
        }
    }
}
