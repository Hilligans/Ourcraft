package dev.hilligans.ourcraft.network.engine;

import dev.hilligans.ourcraft.network.*;
import dev.hilligans.ourcraft.util.Side;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NettyEngine extends NetworkEngine<NettyEngine.NettyNetworkEntity, NettyEngine.NettySocket> {

    public ArrayList<NettySocket> sockets = new ArrayList<>();

    @Override
    public String getResourceName() {
        return "nettyEngine";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    @Override
    public void sendPacketToAll(Protocol protocol, PacketBase<?> packet) {
        ConcurrentLinkedQueue<NettyNetworkEntity> queue = protocolMap.getOrDefault(protocol, null);
        if(queue != null) {
            PacketByteArray array = new PacketByteArray(packet);
            for(NettyNetworkEntity networkEntity : queue) {
                //networkEntity.getChannel().writeAndFlush(array);
            }
        }
    }

    @Override
    public List<NettySocket> getNetworkSockets() {
        return List.of();
    }

    public synchronized void addSocket(NettySocket socket) {
        sockets.add(socket);
    }

    @Override
    public NettySocket openClient(Protocol protocol, String host, String port) {
        NettySocket socket = new NettySocket(protocol, host, Integer.parseInt(port));
        addSocket(socket);
        socket.connectSocket();
        return socket;
    }

    @Override
    public NettySocket openServer(Protocol protocol, String port) {
        NettySocket socket = new NettySocket(protocol, Integer.parseInt(port));
        addSocket(socket);
        socket.connectSocket();
        return socket;
    }

    public static class NettySocket extends ChannelInitializer<SocketChannel> implements NetworkSocket {

        public Side side;
        public Protocol protocol;

        public int port;
        public String host;

        public NettySocket(Protocol protocol, String host, int port) {
            this.protocol = protocol;
            this.port = port;
            this.host = host;
            this.side = Side.CLIENT;
        }

        public NettySocket(Protocol protocol, int port) {
            this.protocol = protocol;
            this.port = port;
            this.side = Side.SERVER;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            System.out.println("initChannel");
            ch.pipeline().addLast(new PacketEncoder(2, false));
            ch.pipeline().addLast(new PacketDecoder(2, false));
            ch.pipeline().addLast(new NettyNetworkEntity(protocol, this));
        }

        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;
        ChannelFuture channelFuture;

        @Override
        public void connectSocket() {
            try {
                workerGroup = new NioEventLoopGroup();
                if(getSide().isClient()) {
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup).channel(NioSocketChannel.class).handler(this);
                    b.connect(host, port).sync();
                } else {
                    bossGroup = new NioEventLoopGroup(1);
                    try {
                        ServerBootstrap b = new ServerBootstrap();
                        b.group(bossGroup, workerGroup)
                                .channel(NioServerSocketChannel.class)
                                .handler(new LoggingHandler(LogLevel.INFO))
                                .childHandler(this);

                        channelFuture = b.bind(port).sync();
                        channelFuture.channel().closeFuture().sync();
                    } finally {
                        bossGroup.shutdownGracefully();
                        workerGroup.shutdownGracefully();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void closeSocket() {

        }

        @Override
        public Side getSide() {
            return side;
        }

        @Override
        public int getPort() {
            return port;
        }

        @Override
        public String getHost() {
            return host;
        }

        @Override
        public Protocol getInitialSendProtocol() {
            return protocol;
        }

        @Override
        public Protocol getInitialReceiveProtocol() {
            return protocol;
        }
    }

    public static class NettyNetworkEntity extends SimpleChannelInboundHandler<IPacketByteArray> implements NetworkEntity {

        Protocol sendProtocol;
        Protocol receiveProtocol;
        Channel channel;
        NetworkEngine<NettyEngine.NettyNetworkEntity, NettyEngine.NettySocket> engine;
        NettySocket socket;

        public boolean alive = false;

        public NettyNetworkEntity(Protocol initialProtocol, NettySocket socket) {
            this.sendProtocol = initialProtocol;
            this.receiveProtocol = initialProtocol;
            this.socket = socket;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.alive = true;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, IPacketByteArray msg) throws Exception {

        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            this.alive = false;
        }

        @Override
        public Protocol getSendProtocol() {
            return sendProtocol;
        }

        @Override
        public Protocol getReceiveProtocol() {
            return receiveProtocol;
        }

        @Override
        public INetworkEngine<?, ?> getNetworkEngine() {
            return engine;
        }

        @Override
        public void setNetworkEngine(INetworkEngine<?, ?> networkEngine) {
            this.engine = (NetworkEngine<NettyNetworkEntity, NettySocket>) networkEngine;
        }

        @Override
        public NetworkSocket getNetworkSocket() {
            return socket;
        }

        @Override
        public void setAlive(boolean alive) {
            if(!alive) {
                channel.close();
            }
        }

        @Override
        public boolean isAlive() {
            return alive;
        }
    }
}
