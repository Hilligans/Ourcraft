package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.ServerMain;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class ServerNetwork extends Network {

    public IServer server;

    public EventLoopGroup bossGroup;
    public EventLoopGroup workerGroup;
    public ChannelFuture channelFuture;

    public ServerNetwork(GameInstance gameInstance, Protocol protocol, IServer server) {
        super(gameInstance, protocol);
        this.server = server;
    }

    public void startServer(String port) throws Exception {
        networkHandler = new ServerNetworkHandler(this, server, gameInstance.getExcept("ourcraft:Auth", Protocol.class), gameInstance.get("ourcraft:Auth", Protocol.class));
        ServerNetworkHandler.debug = Ourcraft.getArgumentContainer().getBoolean("--tracePacket", false);

        final int PORT = Integer.parseInt(System.getProperty("port", port));

        SelfSignedCertificate ssc = new SelfSignedCertificate();
        sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(this);

            channelFuture = b.bind(PORT).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public ServerNetwork debug(boolean debug) {
        super.debug(debug);
        return this;
    }

    public void close() {
        if(bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if(workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
