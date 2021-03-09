package Hilligans.Network.Packet.Server;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SRegisterBlock extends PacketBase {

    String name;
    short id;
    short type;
    byte stateSize;

    public SRegisterBlock() {
        super(23);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(name);
        packetData.writeShort(id);
        packetData.writeShort(type);
        packetData.writeByte(stateSize);
    }

    @Override
    public void decode(PacketData packetData) {
        name = packetData.readString();
        id = packetData.readShort();
        type = packetData.readShort();
        stateSize = packetData.readByte();
    }

    @Override
    public void handle() {

    }
}
