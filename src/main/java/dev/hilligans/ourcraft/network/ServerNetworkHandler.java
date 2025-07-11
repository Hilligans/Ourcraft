package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.debug.PacketTraceByteArray;
import dev.hilligans.ourcraft.network.packet.client.CHandshakePacket;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.network.packet.server.SSendPlayerList;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class ServerNetworkHandler extends SimpleChannelInboundHandler<IPacketByteArray> implements IServerPacketHandler {

    public final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public ArrayList<ChannelId> channelIds = new ArrayList<>();
    public ConcurrentHashMap<ChannelId, ServerPlayerData> mappedPlayerData = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ServerPlayerData> nameToPlayerData = new ConcurrentHashMap<>();

    public static boolean debug = false;
    public boolean ssl = true;

    public IServer server;
    public Protocol defaultReceiveProtocol;
    public Protocol defaultSendProtocol;

    public ServerNetworkHandler(IServer server, Protocol defaultSendProtocol, Protocol defaultReceiveProtocol) {
        this.server = server;
        this.defaultSendProtocol = defaultSendProtocol;
        this.defaultReceiveProtocol = defaultReceiveProtocol;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(ssl) {
            ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                    new GenericFutureListener<Future<Channel>>() {
                        @Override
                        public void operationComplete(Future<Channel> future) throws Exception {
                            // ctx.writeAndFlush(new PacketData(21));
                            channels.add(ctx.channel());
                            channelIds.add(ctx.channel().id());
                        }
                    });
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //TODO handle unloading
        ServerPlayerData serverPlayerData = mappedPlayerData.remove(ctx.channel().id());
        if(serverPlayerData != null) {
            serverPlayerData.handleDisconnect();
            serverPlayerData.close();
            sendPacketInternal(new SSendPlayerList(serverPlayerData.getPlayerName(), (int) serverPlayerData.getPlayerID().l1,false));
            //sendPacketInternal(new SChatMessage(serverPlayerData.getPlayerName() + " has left the game"));
        }
        channels.remove(ctx.channel());
       // mappedChannels.remove(id);
        channelIds.remove(ctx.channel().id());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacketByteArray msg) throws Exception {
        ServerPlayerData serverPlayerData = mappedPlayerData.get(ctx.channel().id());
        PacketBase<?> packetBase;
        if(serverPlayerData == null) {
            packetBase = msg.createPacket(defaultReceiveProtocol);
            if(!(packetBase instanceof CHandshakePacket)) {
                ctx.close();
                return;
            }
            packetBase.handle(this);
        } else {
          //  packetBase = msg.createPacket(serverPlayerData.getReceiveProtocol());
          //  packetBase.handle(serverPlayerData);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void sendPacketInternal(PacketBase<?> packetBase) {
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

    public static ChannelFuture sendPacket(PacketBase<?> packetBase, Channel channel) {
        if(debug) {
            return channel.writeAndFlush(new PacketTraceByteArray(packetBase));
        } else {
            return channel.writeAndFlush(new PacketByteArray(packetBase));
        }
    }

    public ChannelFuture sendPacket(PacketBase<?> packetBase, ChannelHandlerContext ctx) {
        return sendPacket(packetBase, ctx.channel());
    }


    public static ChannelFuture sendPacket1(PacketBase<?> packetBase, ChannelHandlerContext ctx) {
        return sendPacket(packetBase, ctx.channel());
    }

    public static void sendPacketClose(PacketBase<?> packetBase, ChannelHandlerContext ctx) {
        try {
        sendPacket1(packetBase, ctx).addListeners((ChannelFutureListener) future -> {
            if (ctx.channel().isOpen()) {
                ctx.channel().close().awaitUninterruptibly(100);
            }
        });
        } catch (Exception ignored) {}
    }

    public void sendPacket(PacketBase<?> packetBase, PlayerEntity playerEntity) {
        sendPacket(packetBase, playerEntity.getPlayerData().getChannelId());
    }

    public void sendPacket(PacketBase<?> packetBase, ChannelId channelId) {
        sendPacket(packetBase, channels.find(channelId));
    }

    public void sendPacketExcept(PacketBase<?> packetBase, ChannelHandlerContext ctx) {
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

    public PlayerEntity getPlayerEntity(String name) {
        ServerPlayerData serverPlayerData = nameToPlayerData.get(name);
        if(serverPlayerData != null) {
            return serverPlayerData.getPlayerEntity();
        }
        return null;
    }

    @Override
    public IServerWorld getWorld() {
        throw new IllegalStateException("Player data has not been loaded yet in the networking sequence");
    }

    @Override
    public ServerPlayerData getServerPlayerData() {
        throw new IllegalStateException("Player data has not been loaded yet in the networking sequence");
    }

    @Override
    public PlayerEntity getPlayerEntity() {
        throw new IllegalStateException("Player data has not been loaded yet in the networking sequence");
    }

    @Override
    public IServer getServer() {
        return server;
    }

    @Override
    public ServerNetworkHandler getServerNetworkHandler() {
        return this;
    }

    @Override
    public void disconnect(String reason) {

    }
}
