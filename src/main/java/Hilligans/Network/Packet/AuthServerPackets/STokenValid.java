package Hilligans.Network.Packet.AuthServerPackets;

import Hilligans.Network.Packet.Client.CHandshakePacket;
import Hilligans.Network.Packet.Server.SDisconnectPacket;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import io.netty.channel.ChannelHandlerContext;

public class STokenValid extends PacketBase {

    String username;
    String uuid;
    boolean valid;
    String tempid;

    public STokenValid() {
        super(2);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        username = packetData.readString();
        uuid = packetData.readString();
        valid = packetData.readBoolean();
        tempid = packetData.readString();
    }

    @Override
    public void handle() {
        ChannelHandlerContext oldCtx = ServerMain.server.playerQueue.get(tempid).typeA;
        CHandshakePacket packet = ServerMain.server.waitingPlayers.get(oldCtx);
        ServerMain.server.waitingPlayers.remove(oldCtx);
        ServerMain.server.playerQueue.remove(tempid);
        if(valid) {
            CHandshakePacket.handlePlayer(packet.name, packet.version, oldCtx, uuid);
        } else {
            ServerNetworkHandler.sendPacketClose(new SDisconnectPacket("Could not authorize you account"),oldCtx);
        }
        //System.out.println(username + " uuid:" + uuid + " valid:" + valid);

    }
}
