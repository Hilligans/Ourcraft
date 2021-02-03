package Hilligans.Network;

import Hilligans.Network.Packet.Server.SChatMessage;
import Hilligans.ServerMain;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerNetworkHandler extends SimpleChannelInboundHandler<PacketData> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    static ArrayList<ChannelId> channelIds = new ArrayList<>();
    public static HashMap<ChannelId,Integer> mappedId = new HashMap<>();
    public static HashMap<ChannelId,String> mappedName = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                       // ctx.writeAndFlush(new PacketData(21));
                        channels.add(ctx.channel());
                        channelIds.add(ctx.channel().id());
                    }
                });
        //System.out.println("STARTING SERVER");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int id = mappedId.getOrDefault(ctx.channel().id(),-1);
        if(id != -1) {
            ServerMain.world.removeEntity(id);
        }
        sendPacket(new SChatMessage(mappedName.get(ctx.channel().id()) + " has left the game"));
        mappedName.remove(ctx.channel().id());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketData msg) throws Exception {
        PacketBase packetBase = msg.createPacket();
        packetBase.handle();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public static void sendPacket(PacketBase packetBase) {
        for(int x = 0; x < channelIds.size(); x++) {
            Channel channel = channels.find(channelIds.get(x));
            if(channel == null) {
                channelIds.remove(x);
                x--;
                continue;
            }
            channel.writeAndFlush(new PacketData(packetBase));
        }
    }

    public static void sendPacket(PacketBase packetBase, ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new PacketData(packetBase));
    }
}
