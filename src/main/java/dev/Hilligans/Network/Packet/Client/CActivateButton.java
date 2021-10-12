package dev.Hilligans.Network.Packet.Client;

import dev.Hilligans.Client.Rendering.Widgets.Button;
import dev.Hilligans.Client.Rendering.Widgets.Widget;
import dev.Hilligans.Container.Container;
import dev.Hilligans.Data.Other.Server.ServerPlayerData;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.PacketData;
import dev.Hilligans.Network.ServerNetworkHandler;

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
    public void encode(PacketData packetData) {
        packetData.writeShort(button);
    }

    @Override
    public void decode(PacketData packetData) {
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
