package Hilligans.Network;

import Hilligans.Block.Blocks;
import Hilligans.Client.Rendering.Screens.DisconnectScreen;
import Hilligans.Client.Rendering.Screens.JoinScreen;
import Hilligans.Client.Rendering.Textures;
import Hilligans.ClientMain;
import Hilligans.Network.Packet.Client.CHandshakePacket;
import Hilligans.World.ClientWorld;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static org.lwjgl.glfw.GLFW.*;

public class ClientNetworkHandler extends SimpleChannelInboundHandler<PacketData> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ClientNetworkHandler.sendPacket(new CHandshakePacket());
        ClientMain.renderWorld = true;

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Textures.clear();
        System.out.println("DISCONNECTED FROM SERVER");
        if(!ClientMain.valid) {
            //System.out.println("YOUR GAME VERSION MAY BE OUT OF DATE");
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

    public static void sendPacket(PacketBase packetBase) {
        if(ClientNetworkInit.channel != null) {
            ClientNetworkInit.channel.writeAndFlush(new PacketData(packetBase));
        }
    }

}
