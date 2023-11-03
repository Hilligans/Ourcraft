package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.Widget;
import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.Network.IPacketByteArray;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;

public class CActivateButton extends PacketBase {

    short button;

    public CActivateButton() {
        super(25);
    }

    public CActivateButton(int button) {
        this();
        this.button = (short)button;
    }

    @Override
    public void encode(IPacketByteArray packetData) {
        packetData.writeShort(button);
    }

    @Override
    public void decode(IPacketByteArray packetData) {
        button = packetData.readShort();
    }

    @Override
    public void handle() {
        ServerPlayerData serverPlayerData = ServerNetworkHandler.playerData.get(ServerNetworkHandler.mappedId.get(ctx.channel().id()));
        Container container = serverPlayerData.openContainer;
        if(container.widgets.size() > button) {
            Widget widget = container.widgets.get(button);
            if(widget instanceof Button) {
                widget.activate(button,0);
            }
        }
    }
}
