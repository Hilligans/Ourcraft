package Hilligans.Network;

import Hilligans.Client.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.util.ArrayList;
import java.util.LinkedList;

public class ClientNetwork extends Network {

    public Client client;
    public ArrayList<PacketBase> packets = new ArrayList<>();

    public ClientNetwork(Protocol protocol) {
        super(protocol);
    }

    public void joinServer(String ip, String port, Client client) throws Exception {
        this.client = client;

        networkHandler = new ClientNetworkHandler(this);
        ClientNetworkHandler.clientNetworkHandler = (ClientNetworkHandler) networkHandler;

        final String HOST = System.getProperty("host", ip);
        final int PORT = Integer.parseInt(System.getProperty("port", port));
        sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(this);
            ((NetworkHandler) networkHandler).setData(b.connect(HOST, PORT).sync().channel(), group, ip, port);
        } finally {
        }
        for (PacketBase packet : packets) {
            ((ClientNetworkHandler) networkHandler).sendPacket(packet);
        }
    }

    @Override
    public void sendPacket(PacketBase packetBase) {
        if(networkHandler != null && ((ClientNetworkHandler)networkHandler).enabled) {
            ((ClientNetworkHandler)networkHandler).sendPacket(packetBase);
        } else {
            packets.add(packetBase);
        }
    }
}
