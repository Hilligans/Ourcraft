package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.client.rendering.screens.DisconnectScreen;
import dev.hilligans.ourcraft.network.packet.client.CHandshakePacket;
import io.netty.channel.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class ClientNetworkHandler extends NetworkHandler {

    public static ClientNetworkHandler clientNetworkHandler;

    public ClientNetwork network;
    public ConcurrentLinkedQueue<PacketBase> packets = new ConcurrentLinkedQueue<>();

    public ClientNetworkHandler(ClientNetwork network) {
        this.network = network;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        network.client.sendPacket(new CHandshakePacket(network.client.playerData.userName, network.client.playerData.authToken));
        network.client.renderWorld = true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("DISCONNECTED FROM SERVER");
        network.client.renderWorld = false;
        network.client.valid = false;
        //network.client.clientWorld = new ClientWorld(network.client);
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacketByteArray msg) throws Exception {
        PacketBase packetBase = msg.createPacket(network.receiveProtocol);
        packets.add(packetBase);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        cause.printStackTrace();
        //network.client.clientWorld = new ClientWorld(network.client);
        network.client.openScreen(new DisconnectScreen(network.client,cause.getMessage()));
    }

    public void processPackets() {
        PacketBase packetBase;
        while((packetBase = packets.poll()) != null) {
            if(packetBase instanceof PacketBaseNew<?> packetBaseNew) {
                packetBaseNew.handle(network.client);
            } else {
                packetBase.handle();
            }
        }
    }
}
