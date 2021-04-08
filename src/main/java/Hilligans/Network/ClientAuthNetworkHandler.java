package Hilligans.Network;

import Hilligans.Client.Rendering.Screens.DisconnectScreen;
import Hilligans.ClientMain;
import Hilligans.World.ClientWorld;
import io.netty.channel.*;

public class ClientAuthNetworkHandler extends NetworkHandler {

    public static ClientAuthNetworkHandler networkHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketData msg) throws Exception {
        PacketBase packetBase = msg.createAltPacket();
        packetBase.handle();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public static ChannelFuture sendPacketDirect(PacketBase packetBase) {
        if(networkHandler == null || !networkHandler.channel.isOpen()) {
            networkHandler = new ClientAuthNetworkHandler();
            try {
                ClientNetworkInit.joinServer("72.172.99.188", "25588", networkHandler);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return networkHandler.sendPacket(packetBase);
    }

}
