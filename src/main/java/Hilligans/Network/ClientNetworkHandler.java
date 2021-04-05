package Hilligans.Network;

import Hilligans.Client.Rendering.Screens.DisconnectScreen;
import Hilligans.Client.Rendering.Textures;
import Hilligans.ClientMain;
import Hilligans.Network.Packet.Client.CHandshakePacket;
import Hilligans.World.ClientWorld;
import io.netty.channel.*;

public class ClientNetworkHandler extends NetworkHandler {

    public static ClientNetworkHandler clientNetworkHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ClientNetworkHandler.sendPacketDirect(new CHandshakePacket());
        ClientMain.renderWorld = true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Textures.clear();
        System.out.println("DISCONNECTED FROM SERVER");

        if(!ClientMain.valid) {
        }
        ClientMain.renderWorld = false;
        ClientMain.clientWorld = new ClientWorld();

        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketData msg) throws Exception {
        PacketBase packetBase = msg.createPacket();
        packetBase.handle();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        ClientMain.clientWorld = new ClientWorld();
        ClientMain.openScreen(new DisconnectScreen(cause.getMessage()));
    }

    public static void sendPacketDirect(PacketBase packetBase) {
     //  if(clientNetworkHandler.channel != null && clientNetworkHandler.channel.isOpen()) {
        if(clientNetworkHandler != null) {
            clientNetworkHandler.channel.writeAndFlush(new PacketData(packetBase));
        }
       // }
    }

    public static boolean close() {
        if(clientNetworkHandler != null && clientNetworkHandler.channel != null) {
            clientNetworkHandler.channel.close();
            return true;
        }
        return false;
    }

}
