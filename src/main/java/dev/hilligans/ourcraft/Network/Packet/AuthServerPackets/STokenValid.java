package dev.hilligans.ourcraft.Network.Packet.AuthServerPackets;

import dev.hilligans.ourcraft.Network.*;
import dev.hilligans.ourcraft.Network.Packet.Client.CHandshakePacket;
import dev.hilligans.ourcraft.Network.Packet.Server.SDisconnectPacket;
import dev.hilligans.ourcraft.ServerMain;
import io.netty.channel.ChannelHandlerContext;

public class STokenValid extends PacketBaseNew<IServerPacketHandler> {

    String username;
    String uuid;
    boolean valid;
    String tempid;

    public STokenValid() {
        super(2);
    }

    @Override
    public void encode(IPacketByteArray packetData) {}

    @Override
    public void decode(IPacketByteArray packetData) {
        username = packetData.readString();
        uuid = packetData.readString();
        valid = packetData.readBoolean();
        tempid = packetData.readString();
    }

    @Override
    public void handle(IServerPacketHandler serverPacketHandler) {
        ChannelHandlerContext oldCtx = ServerMain.getServer().playerQueue.get(tempid).typeA;
        CHandshakePacket packet = ServerMain.getServer().waitingPlayers.get(oldCtx);
        ServerMain.getServer().waitingPlayers.remove(oldCtx);
        ServerMain.getServer().playerQueue.remove(tempid);
        //System.out.println(this.toString());
        if(valid) {
           // CHandshakePacket.handlePlayer(packet.name, packet.version, oldCtx, uuid);
        } else {
            ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("Could not authorize you account"),oldCtx);
        }
    }

    @Override
    public String toString() {
        return "STokenValid{" +
                "username='" + username + '\'' +
                ", uuid='" + uuid + '\'' +
                ", valid=" + valid +
                ", tempid='" + tempid + '\'' +
                '}';
    }
}
