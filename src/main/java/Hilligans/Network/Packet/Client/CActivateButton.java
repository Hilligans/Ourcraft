package Hilligans.Network.Packet.Client;

import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Container.Container;
import Hilligans.Data.Other.Server.ServerPlayerData;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;

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
