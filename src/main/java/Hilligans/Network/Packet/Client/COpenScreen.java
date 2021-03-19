package Hilligans.Network.Packet.Client;

import Hilligans.Client.Rendering.ContainerScreen;
import Hilligans.Client.Rendering.Screen;
import Hilligans.Container.Containers.CreativeContainer;
import Hilligans.Data.Other.Server.PlayerData;
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
            PlayerData playerData = ServerNetworkHandler.getPlayerData(ctx);
            if(playerData != null) {
                CreativeContainer creativeContainer = new CreativeContainer(playerData.playerInventory,CreativeContainer.createInventory());
                playerData.openContainer(creativeContainer);
                ServerNetworkHandler.sendPacket(new SOpenContainer(creativeContainer), ctx);
            }
        }
    }
}
