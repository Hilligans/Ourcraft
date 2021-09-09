package Hilligans.Network;

import Hilligans.Container.Container;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;

public class ServerNetworkInit extends ChannelInitializer<SocketChannel> {

    public static void startServer(String port) throws Exception {
        PacketBase.register();
        Container.register();

        final int PORT = Integer.parseInt(System.getProperty("port", port));

        SelfSignedCertificate ssc = new SelfSignedCertificate();
        SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerNetworkInit(sslCtx));



            b.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private final SslContext sslCtx;

    public ServerNetworkInit(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(sslCtx.newHandler(ch.alloc()));

        // On top of the SSL handler, add the text line codec.
        //pipeline.addLast(new DelimiterBasedFrameDecoder(65536, Delimiters.lineDelimiter()));


        pipeline.addLast(new PacketEncoder(2,false));
        pipeline.addLast(new PacketDecoder(2,false));
//        pipeline.addLast(new StringDecoder());

        // and then business logic.
        pipeline.addLast(new ServerNetworkHandler());
    }

    public static final int MIN_PORT_NUMBER = 20000;
    public static final int MAX_PORT_NUMBER = 70000;

    public static int getOpenPort() throws Exception {
        int size = MAX_PORT_NUMBER - MIN_PORT_NUMBER;
        for(int x = 0; x < 100; x++) {
            int port = (int) (Math.random() * size + MIN_PORT_NUMBER);
            if(available(port)) {
                return port;
            }
        }
        for(int x = MIN_PORT_NUMBER;x < MAX_PORT_NUMBER;x++) {
            if(available(x)) {
                return x;
            }
        }
        throw new Exception("Failed to find open port, something is probably wrong");
    }

    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {}
            }
        }
        return false;
    }
}
