package dev.hilligans.ourcraft.Network;

import dev.hilligans.ourcraft.Network.Debug.PacketTrace;
import dev.hilligans.ourcraft.Network.Debug.PacketTracePacketDecoder;
import dev.hilligans.ourcraft.Network.Debug.PacketTracePacketEncoder;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class Network extends ChannelInitializer<SocketChannel> {

    public SimpleChannelInboundHandler<IPacketByteArray> networkHandler;
    public SslContext sslCtx;
    public ChannelPipeline channelPipeline;

    public Protocol sendProtocol;
    public Protocol receiveProtocol;
    public int packetIdWidth;
    public boolean compressed;
    public boolean debug = false;


    public Network(Protocol protocol) {
        this(protocol,protocol,2);
    }

    public Network(Protocol sendProtocol, Protocol receiveProtocol, int packetIdWidth) {
        this(sendProtocol,receiveProtocol,packetIdWidth,false);
    }

    public Network(Protocol sendProtocol, Protocol receiveProtocol, int packetIdWidth, boolean compressed) {
        this.sendProtocol = sendProtocol;
        this.receiveProtocol = receiveProtocol;
        this.packetIdWidth = packetIdWidth;
        this.compressed = compressed;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        if(debug) {
            pipeline.addLast(new PacketEncoder(packetIdWidth, compressed));
            pipeline.addLast(new PacketDecoder(packetIdWidth, compressed));
        } else {
            pipeline.addLast(new PacketTracePacketEncoder(packetIdWidth, compressed, PacketTrace.PACKET_TRACE));
            pipeline.addLast(new PacketTracePacketDecoder(packetIdWidth, compressed, PacketTrace.PACKET_TRACE));
        }
        pipeline.addLast(networkHandler);
    }

    public Network debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public void sendPacket(PacketBase packetBase) {}

    public void sendPacketDirect(PacketBase packetBase) {}

    public void disconnect() {
        if(networkHandler instanceof ClientNetworkHandler) {
            ((ClientNetworkHandler) networkHandler).channel.close();
        }
    }
}
