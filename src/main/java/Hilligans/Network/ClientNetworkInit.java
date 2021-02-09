package Hilligans.Network;

import Hilligans.Container.Container;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class ClientNetworkInit extends ChannelInitializer<SocketChannel> {

    public static Channel channel;

    private final SslContext sslCtx;

    public ClientNetworkInit(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    public static EventLoopGroup group;

    public static void joinServer(String ip, String port) throws Exception {

        final String HOST = System.getProperty("host", ip);
        final int PORT = Integer.parseInt(System.getProperty("port", port));

        final SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientNetworkInit(sslCtx));

            // Start the connection attempt.
            channel = b.connect(HOST, PORT).sync().channel();
            //}
        } finally {
            // The connection is closed automatically on shutdown.
        }
    }

    public static void close() {
        if(group != null) {
            group.shutdownGracefully();
        }
    }




    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));

        // On top of the SSL handler, add the text line codec.

        //pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        //pipeline.addLast(new DelimiterBasedFrameDecoder(65536, Delimiters.lineDelimiter()));


        pipeline.addLast(new PacketDecoder());
        pipeline.addLast(new PacketEncoder());
//        pipeline.addLast(new StringDecoder());




        // and then business logic.
        pipeline.addLast(new ClientNetworkHandler());
    }
}
