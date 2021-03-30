package Hilligans.Network.Packet.Client;

import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screen;
import Hilligans.Container.Containers.CreativeContainer;
import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Network.Packet.Server.SOpenContainer;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;

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
        super(27);
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
