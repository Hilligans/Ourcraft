package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.client.rendering.screens.DisconnectScreen;
import dev.hilligans.ourcraft.network.packet.client.CHandshakePacket;
import io.netty.channel.*;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientNetworkHandler extends NetworkHandler implements NetworkProfile{

    public ClientNetwork network;
    public ConcurrentLinkedQueue<PacketBase<?>> packets = new ConcurrentLinkedQueue<>();
    public Protocol sendProtocol;
    public Protocol receiveProtocol;

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
        System.out.println("CLIENT " + network.client.rWindow.getWindowName()+" DISCONNECTED FROM SERVER");
        //System.out.println(STR."CLIENT \{network.client.rWindow.getWindowName()} DISCONNECTED FROM SERVER");
        //System.out.println(channel.closeFuture().sync().exceptionNow().getMessage());
        network.client.renderWorld = false;
        network.client.valid = false;
        //network.client.clientWorld = new ClientWorld(network.client);
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacketByteArray msg) throws Exception {
        PacketBase<?> packetBase = msg.createPacket(receiveProtocol);
        packets.add(packetBase);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        network.client.openScreen(new DisconnectScreen(network.client,cause.getMessage()));
    }

    public void processPackets() {
        PacketBase<?> packetBase;
        while((packetBase = packets.poll()) != null) {
            packetBase.handle(network.client);
        }
    }

    @Override
    public void setSendProtocol(Protocol protocol) {
        this.sendProtocol = protocol;
    }

    @Override
    public void setReceiveProtocol(Protocol protocol) {
        this.receiveProtocol = protocol;
    }

    @Override
    public Protocol getSendProtocol() {
        return this.sendProtocol;
    }

    @Override
    public Protocol getReceiveProtocol() {
        return this.receiveProtocol;
    }

    @Override
    public void setChannel(Channel channel) {

    }

    @Override
    public Channel getChannel() {
        return null;
    }
}
