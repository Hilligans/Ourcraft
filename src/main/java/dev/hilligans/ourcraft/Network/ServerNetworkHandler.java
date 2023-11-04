package dev.hilligans.ourcraft.Network;

import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.Network.Debug.PacketTraceByteArray;
import dev.hilligans.ourcraft.Network.Packet.Client.CHandshakePacket;
import dev.hilligans.ourcraft.Network.Packet.Server.SChatMessage;
import dev.hilligans.ourcraft.Data.Other.Server.ServerPlayerData;
import dev.hilligans.ourcraft.Network.Packet.Server.SSendPlayerList;
import dev.hilligans.ourcraft.ServerMain;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;

public class  ServerNetworkHandler extends SimpleChannelInboundHandler<IPacketByteArray> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    static ArrayList<ChannelId> channelIds = new ArrayList<>();
    public static HashMap<ChannelId,Integer> mappedId = new HashMap<>();
    public static HashMap<ChannelId,String> mappedName = new HashMap<>();
    public static HashMap<String, ChannelId> nameToChannel = new HashMap<>();
    public static Int2ObjectOpenHashMap<ChannelId> mappedChannels = new Int2ObjectOpenHashMap<>();
    public static Int2ObjectOpenHashMap<ServerPlayerData> playerData = new Int2ObjectOpenHashMap<>();

    public ServerNetwork network;
    public static boolean debug = false;

    public ServerNetworkHandler(ServerNetwork network) {
        this.network = network;
    }

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
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int id = mappedId.getOrDefault(ctx.channel().id(),-1);
        if(id != -1) {
            int dim = ServerNetworkHandler.getPlayerData(ctx).getDimension();
            ServerMain.getWorld(dim).removeEntity(id);
            mappedChannels.remove(id);
            playerData.remove(id).close();
        }
        channelIds.remove(ctx.channel().id());
        sendPacket(new SSendPlayerList(mappedName.get(ctx.channel().id()),id,false));
        sendPacket(new SChatMessage(mappedName.get(ctx.channel().id()) + " has left the game"));
        nameToChannel.remove(mappedName.remove(ctx.channel().id()));
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacketByteArray msg) throws Exception {
        PacketBase packetBase = msg.createPacket(network.receiveProtocol);
        if(!(packetBase instanceof CHandshakePacket)) {
            if (mappedId.getOrDefault(ctx.channel().id(), Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                ctx.close();
                return;
            }
        }
        try {
            if (packetBase instanceof PacketBaseNew<?> packetBaseNew) {
                packetBaseNew.handle(getPlayerData(ctx));
            } else {
                packetBase.handle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            sendPacket(packetBase, channel);
        }
    }

    public static ChannelFuture sendPacket(PacketBase packetBase, Channel channel) {
        if(debug) {
            return channel.writeAndFlush(new PacketTraceByteArray(packetBase));
        } else {
            return channel.writeAndFlush(new PacketByteArray(packetBase));
        }
    }

    public static ChannelFuture sendPacket(PacketBase packetBase, ChannelHandlerContext ctx) {
        return sendPacket(packetBase, ctx.channel());
    }

    public static void sendPacketClose(PacketBase packetBase, ChannelHandlerContext ctx) {
        try {
        sendPacket(packetBase, ctx).addListeners((ChannelFutureListener) future -> {
            if (ctx.channel().isOpen()) {
                ctx.channel().close().awaitUninterruptibly(100);
            }
        });
        } catch (Exception ignored) {}
    }

    public static void sendPacket(PacketBase packetBase, PlayerEntity playerEntity) {
        sendPacket(packetBase, channels.find(mappedChannels.get(playerEntity.id)));
    }

    public static void sendPacket(PacketBase packetBase, ChannelId channelId) {
        sendPacket(packetBase, channels.find(channelId));
    }

    public static void sendPacket(PacketBase packetBase, int channelId) {
        ChannelId channelId1 = mappedChannels.get(channelId);
        if(channelId1 != null) {
            Channel channel = channels.find(channelId1);
            if (channel != null) {
                sendPacket(packetBase, channel);
            }
        }
    }

    public static void sendPacketExcept(PacketBase packetBase, ChannelHandlerContext ctx) {
        for(int x = 0; x < channelIds.size(); x++) {
            Channel channel = channels.find(channelIds.get(x));
            if(channel == null) {
                channelIds.remove(x);
                x--;
                continue;
            }
            if(!channel.id().equals(ctx.channel().id())) {
                sendPacket(packetBase, channel);
            }
        }
    }

    public static ServerPlayerData getPlayerData(ChannelHandlerContext ctx) {
        return playerData.get(mappedId.get(ctx.channel().id()));
    }

    public static PlayerEntity getPlayerEntity(String name) {
        ChannelId channelId = nameToChannel.get(name);
        if(channelId != null) {
            int id = mappedId.getOrDefault(channelId,Integer.MIN_VALUE);
            if(id != Integer.MIN_VALUE) {
                ServerPlayerData serverPlayerData = playerData.get(id);
                if(serverPlayerData != null) {
                    return serverPlayerData.playerEntity;
                }
            }
        }
        return null;
    }
}
