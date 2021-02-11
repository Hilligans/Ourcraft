package Hilligans.Network.Packet.Client;

import Hilligans.Container.Containers.InventoryContainer;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.Server.PlayerData;

public class CCloseScreen extends PacketBase {

    public CCloseScreen() {
        super(20);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {
        PlayerData playerData = ServerNetworkHandler.getPlayerData(ctx);
        playerData.openContainer(new InventoryContainer(playerData.playerInventory));
    }
}
