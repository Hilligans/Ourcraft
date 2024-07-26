package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.network.debug.PacketTrace;
import dev.hilligans.ourcraft.network.debug.PacketTracePacketDecoder;
import dev.hilligans.ourcraft.network.debug.PacketTracePacketEncoder;
import io.netty.channel.*;
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

    public Network(GameInstance gameInstance, Protocol protocol) {
        this(gameInstance, protocol,protocol,2);
        System.out.println("yes1");
    }

    public Network(GameInstance gameInstance, Protocol sendProtocol, Protocol receiveProtocol, int packetIdWidth) {
        System.out.println("Instance" + gameInstance);
        this.gameInstance = gameInstance;
        this.packetIdWidth = packetIdWidth;
        this.compressed = false;
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

    public void sendPacket(PacketBase<?> packetBase) {}

    public void sendPacketDirect(PacketBase<?> packetBase) {}

    public void disconnect() {
        if(networkHandler instanceof ClientNetworkHandler) {
            ((ClientNetworkHandler) networkHandler).channel.close();
        }
    }
}
