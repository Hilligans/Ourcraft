package Hilligans.Network;

import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SChatMessage;
import Hilligans.Data.Other.Server.PlayerData;
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
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerNetworkHandler extends SimpleChannelInboundHandler<PacketData> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    static ArrayList<ChannelId> channelIds = new ArrayList<>();
    public static HashMap<ChannelId,Integer> mappedId = new HashMap<>();
    public static HashMap<ChannelId,String> mappedName = new HashMap<>();
    public static HashMap<String, ChannelId> nameToChannel = new HashMap<>();
    public static Int2ObjectOpenHashMap<ChannelId> mappedChannels = new Int2ObjectOpenHashMap<>();
    public static Int2ObjectOpenHashMap<PlayerData> playerData = new Int2ObjectOpenHashMap<>();

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
            mappedChannels.remove(id);
            playerData.remove(id).close();
        }
        channelIds.remove(ctx.channel().id());
        sendPacket(new SChatMessage(mappedName.get(ctx.channel().id()) + " has left the game"));
        nameToChannel.remove(mappedName.remove(ctx.channel().id()));
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

    public static Channel getChannel(int id) {
        //System.out.println(mappedChannels.get(id));
        return channels.find(mappedChannels.get(id));
    }

    public static void sendPacket(PacketBase packetBase, ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new PacketData(packetBase));
    }

    public static void sendPacket(PacketBase packetBase, PlayerEntity playerEntity) {
        channels.find(mappedChannels.get(playerEntity.id)).writeAndFlush(new PacketData(packetBase));
    }

    public static void sendPacket(PacketBase packetBase, ChannelId channelId) {
        channels.find(channelId).writeAndFlush(new PacketData(packetBase));
    }

    public static void sendPacket(PacketBase packetBase, int channelId) {
        ChannelId channelId1 = mappedChannels.get(channelId);
        if(channelId1 != null) {
            Channel channel = channels.find(channelId1);
            if (channel != null) {
                channel.writeAndFlush(new PacketData(packetBase));
            }
        }
    }

    public static PlayerData getPlayerData(ChannelHandlerContext ctx) {
        return playerData.get(mappedId.get(ctx.channel().id()));
    }
}
