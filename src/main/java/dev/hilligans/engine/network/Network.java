package dev.hilligans.engine.network;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.network.debug.PacketTrace;
import dev.hilligans.engine.network.debug.PacketTracePacketDecoder;
import dev.hilligans.engine.network.debug.PacketTracePacketEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class Network extends ChannelInitializer<SocketChannel> {

    public SimpleChannelInboundHandler<IPacketByteArray> networkHandler;
    public SslContext sslCtx;
    public ChannelPipeline channelPipeline;

    public int packetIdWidth;
    public boolean compressed;
    public boolean debug = false;
    public GameInstance gameInstance;

    public Protocol prot;

    public Network(GameInstance gameInstance, Protocol protocol) {
        this(gameInstance, protocol,protocol,2);
    }

    public Network(GameInstance gameInstance, Protocol sendProtocol, Protocol receiveProtocol, int packetIdWidth) {
        this.gameInstance = gameInstance;
        this.packetIdWidth = packetIdWidth;
        this.compressed = false;
        this.prot = sendProtocol;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        if(!debug) {
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
}
