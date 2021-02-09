package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Container.Container;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SOpenContainer extends PacketBase {

    short id;

    public SOpenContainer() {
        super(16);
    }

    public SOpenContainer(Container container) {
        this();
        id = (short) container.type;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeShort(id);
    }

    @Override
    public void decode(PacketData packetData) {
        id = packetData.readShort();
    }

    @Override
    public void handle() {
        Container container = Container.containers.get(id).getContainer();
        if(container != null) {
            ClientMain.openScreen(container);
        }
    }
}
