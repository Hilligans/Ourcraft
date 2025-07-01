package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.GameInstance;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.util.ArrayList;

public class ClientNetwork extends Network {

    public Client client;
    public GameInstance gameInstance;
    public ArrayList<PacketBase<?>> packets = new ArrayList<>();

    public ClientNetwork(GameInstance gameInstance, Protocol protocol) {
        super(gameInstance, protocol);
    }

    public ClientNetwork(GameInstance gameInstance, Protocol sendProtocol, Protocol receiveProtocol, int packetIdWidth) {
        super(gameInstance, sendProtocol, receiveProtocol, packetIdWidth);
    }

    public void joinServer(String ip, String port, Client client) throws Exception {
        this.client = client;
        this.gameInstance = client.gameInstance;

        ClientNetworkHandler net = new ClientNetworkHandler(this);
        net.setSendProtocol(prot);
        net.setReceiveProtocol(prot);
        networkHandler = net;

        final String HOST = System.getProperty("host", ip);
        final int PORT = Integer.parseInt(System.getProperty("port", port));
        sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(this);
            ((NetworkHandler) networkHandler).setData(b.connect(HOST, PORT).sync().channel(), group, ip, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        flush();
    }

    @Override
    public ClientNetwork debug(boolean debug) {
        super.debug(debug);
        return this;
    }

    @Override
    public void sendPacket(PacketBase<?> packetBase) {
       // System.out.println("Sending packet:" + packetBase.getClass());
        if(networkHandler != null && ((ClientNetworkHandler)networkHandler).enabled) {
         //   packetBase.packetId = sendProtocol.packetMap.get(packetBase.getClass());
            sendPacketDirect(packetBase);
        } else {
            packets.add(packetBase);
        }
    }

    public void flush() {
        for (PacketBase<?> packet : packets) {
            ((ClientNetworkHandler) networkHandler).sendPacket(packet);
        }
    }

    @Override
    public void sendPacketDirect(PacketBase<?> packetBase) {
        ((ClientNetworkHandler)networkHandler).sendPacket(packetBase);
    }

    public void processPackets() {
        if(networkHandler != null) {
            ((ClientNetworkHandler) networkHandler).processPackets();
        }
    }
}
