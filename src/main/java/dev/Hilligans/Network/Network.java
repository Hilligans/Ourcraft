package dev.Hilligans.Network;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class Network extends ChannelInitializer<SocketChannel> {

    public SimpleChannelInboundHandler<PacketData> networkHandler;
    public SslContext sslCtx;
    public ChannelPipeline channelPipeline;

    public Protocol sendProtocol;
    public Protocol receiveProtocol;
    public int packetIdWidth;
    public boolean compressed;


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
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        pipeline.addLast(new PacketEncoder(packetIdWidth,compressed));
        pipeline.addLast(new PacketDecoder(packetIdWidth,compressed));
        pipeline.addLast(networkHandler);
    }

    public void sendPacket(PacketBase packetBase) {}

    public void sendPacketDirect(PacketBase packetBase) {}

    public void disconnect() {
        if(networkHandler instanceof ClientNetworkHandler) {
            ((ClientNetworkHandler) networkHandler).channel.close();
        }
    }
}
