package Hilligans.Network.Packet.Server;

import Hilligans.ClientMain;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.World.ClientWorld;

public class SSendBlockChanges extends PacketBase {
    int x;
    int y;
    int z;
    int blockId;

    public SSendBlockChanges(int x, int y, int z, int id) {
        super(4);
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockId = id;
    }

    public SSendBlockChanges() {
        this(0,0,0,0);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeInt(x);
        packetData.writeInt(y);
        packetData.writeInt(z);
        packetData.writeInt(blockId);
    }

    @Override
    public void decode(PacketData packetData) {
        x = packetData.readInt();
        y = packetData.readInt();
        z = packetData.readInt();
        blockId = packetData.readInt();
    }


    @Override
    public void handle() {
        ClientMain.clientWorld.blockChanges.add(new ClientWorld.BlockChange(x,y,z,blockId));
    }
}
