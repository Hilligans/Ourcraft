package dev.Hilligans.Ourcraft.Network.Packet.Client;

import dev.Hilligans.Ourcraft.Client.Rendering.ContainerScreen;
import dev.Hilligans.Ourcraft.Client.Rendering.Screen;
import dev.Hilligans.Ourcraft.Container.Containers.CreativeContainer;
import dev.Hilligans.Ourcraft.Data.Other.Server.ServerPlayerData;
import dev.Hilligans.Ourcraft.Network.Packet.Server.SOpenContainer;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.Network.PacketData;
import dev.Hilligans.Ourcraft.Network.ServerNetworkHandler;

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
    public void encode(PacketData packetData) {
        packetData.writeShort(id);
        packetData.writeBoolean(containerScreen);
    }

    @Override
    public void decode(PacketData packetData) {
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
