package Hilligans.Network;

import Hilligans.Client.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class Network extends ChannelInitializer<SocketChannel> {

    public SimpleChannelInboundHandler<PacketData> networkHandler;
    public Protocol protocol;
    public SslContext sslCtx;

    public Network(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        pipeline.addLast(new PacketEncoder());
        pipeline.addLast(new PacketDecoder());
        pipeline.addLast(networkHandler);
    }

    public void sendPacket(PacketBase packetBase) {}

    public void disconnect() {
        if(networkHandler instanceof ClientNetworkHandler) {
            ((ClientNetworkHandler) networkHandler).channel.close();
        }
    }
}
