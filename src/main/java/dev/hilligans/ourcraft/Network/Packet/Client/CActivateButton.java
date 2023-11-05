package dev.hilligans.ourcraft.Network.Packet.Client;

import dev.hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.Widget;
import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.Network.*;

public class CActivateButton extends PacketBaseNew<IServerPacketHandler> {

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
    public void handle(IServerPacketHandler serverPacketHandler) {
        ServerPlayerData serverPlayerData = serverPacketHandler.getServerPlayerData();
        Container container = serverPlayerData.openContainer;
        if(container.widgets.size() > button) {
            Widget widget = container.widgets.get(button);
            if(widget instanceof Button) {
                widget.activate(button,0);
            }
        }
    }
}
