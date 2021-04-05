package Hilligans.Network;

import Hilligans.ClientMain;
import Hilligans.Container.Container;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.TrustManagerFactory;

public class ClientNetworkInit extends ChannelInitializer<SocketChannel> {

    public static Channel authChannel;

    private final SslContext sslCtx;
    private final SimpleChannelInboundHandler<PacketData> networkHandler;

    public ClientNetworkInit(SslContext sslCtx, SimpleChannelInboundHandler<PacketData> networkHandler) {
        this.sslCtx = sslCtx;
        this.networkHandler = networkHandler;
    }


    public static void joinServer(String ip, String port, NetworkHandler networkHandler) throws Exception {
        final String HOST = System.getProperty("host", ip);
        final int PORT = Integer.parseInt(System.getProperty("port", port));
        //TrustManagerFactory.
        final SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        //final SslContext s slCtx1 = SslContextBuilder.forClient().trustManager(TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ClientNetworkInit(sslCtx, networkHandler));
            networkHandler.setData(b.connect(HOST, PORT).sync().channel(), group, ip, port);
        } finally {}
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
        pipeline.addLast(networkHandler);
    }
}
