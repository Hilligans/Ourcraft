package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.ServerMain;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class ServerNetwork extends Network {

    public GameInstance gameInstance;
    public IServer server;

    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;

    public ServerNetwork(Protocol protocol, IServer server) {
        super(protocol);
        this.server = server;
    }

    public void startServer(String port) throws Exception {
        networkHandler = new ServerNetworkHandler(this, server);
        ServerNetworkHandler.debug = ServerMain.argumentContainer.getBoolean("--tracePacket", false);

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

            b.bind(PORT).sync().channel().closeFuture().sync();
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
    }
}
