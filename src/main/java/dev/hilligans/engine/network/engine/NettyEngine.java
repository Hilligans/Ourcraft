package dev.hilligans.engine.network.engine;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.application.IServerApplication;
import dev.hilligans.engine.network.*;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.engine.network.packet.PacketType;
import dev.hilligans.ourcraft.network.packet.SServerExceptionPacket;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.engine.util.Side;
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
import java.util.function.Consumer;

public class NettyEngine extends NetworkEngine<NettyEngine.NettyNetworkEntity, NettyEngine.NettySocket> {

    public ArrayList<NettySocket> sockets = new ArrayList<>();

    @Override
    public String getResourceName() {
        return "nettyEngine";
    }

    @Override
    public String getResourceOwner() {
        return Engine.ENGINE_NAME;
    }

    @Override
    public List<NettySocket> getNetworkSockets() {
        return List.of();
    }

    public synchronized void addSocket(NettySocket socket) {
        sockets.add(socket);
    }

    @Override
    public NettySocket openClient(Protocol protocol, Client client, String host, String port) {
        NettySocket socket = new NettySocket(this, protocol, client, host, Integer.parseInt(port));
        addSocket(socket);
        return socket;
    }

    @Override
    public NettySocket openServer(Protocol protocol, IServer server, String port) {
        NettySocket socket = new NettySocket(this, protocol, server, Integer.parseInt(port));
        addSocket(socket);
        return socket;
    }

    @Override
    public IByteArray allocByteArray() {
        return new PacketByteArray();
    }

    @Override
    public void freeByteArray(IByteArray array) {
    }

    public static class NettySocket extends ChannelInitializer<SocketChannel> implements NetworkSocket<NettyEngine.NettyNetworkEntity> {

        public Side side;
        public Protocol protocol;
        public NettyEngine engine;

        public int port;
        public String host;

        public Consumer<NettyNetworkEntity> callback;

        public Client client;
        public IServer server;

        public NettySocket(NettyEngine engine, Protocol protocol, Client client, String host, int port) {
            this.engine = engine;
            this.protocol = protocol;
            this.client = client;
            this.port = port;
            this.host = host;
            this.side = Side.CLIENT;
        }

        public NettySocket(NettyEngine engine, Protocol protocol, IServer server, int port) {
            this.engine = engine;
            this.protocol = protocol;
            this.server = server;
            this.port = port;
            this.side = Side.SERVER;
        }

        @Override
        public void onConnected(Consumer<NettyNetworkEntity> callback) {
            this.callback = callback;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            System.out.println("Channel Active:");
            ch.pipeline().addLast(new PacketEncoder(2, false));
            ch.pipeline().addLast(new PacketDecoder(2, false));
            ch.pipeline().addLast(new NettyNetworkEntity(protocol, this).setNetworkEngine(engine));
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

    public static class NettyNetworkEntity extends SimpleChannelInboundHandler<IPacketByteArray> implements NetworkEntity, ClientNetworkEntity<IClientApplication>, ServerNetworkEntity<IServerApplication> {

        Protocol sendProtocol;
        Protocol receiveProtocol;
        Channel channel;
        NetworkEngine<NettyEngine.NettyNetworkEntity, NettyEngine.NettySocket> engine;
        NettySocket socket;

        public boolean alive = false;

        public ServerPlayerData data;

        public NettyNetworkEntity(Protocol initialProtocol, NettySocket socket) {
            this.sendProtocol = initialProtocol;
            this.receiveProtocol = initialProtocol;
            this.socket = socket;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.alive = true;
            this.channel = ctx.channel();
            if(this.socket.callback != null) {
                this.socket.callback.accept(this);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, IPacketByteArray msg) throws Exception {
            try {
                PacketType packet = getReceiveProtocol().fromIdToPacketType(msg.readShort());
                packet.decode(this, msg);
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                if(socket.side.isServer()) {
                    SServerExceptionPacket.send(this, e);
                }
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            this.alive = false;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
        }

        @Override
        public void switchProtocol(Protocol sendProtocol, Protocol receiveProtocol) {
            this.sendProtocol = sendProtocol;
            this.receiveProtocol = receiveProtocol;
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
        public NettyNetworkEntity setNetworkEngine(INetworkEngine<?, ?> networkEngine) {
            this.engine = (NetworkEngine<NettyNetworkEntity, NettySocket>) networkEngine;
            return this;
        }

        @Override
        public NetworkSocket<NettyEngine.NettyNetworkEntity> getNetworkSocket() {
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

        @Override
        public void sendPacket(IByteArray packet) {
            channel.writeAndFlush(packet);
        }

        @Override
        public GameInstance getGameInstance() {
            return socket.engine.getGameInstance();
        }

        @Override
        public Client getClient() {
            return socket.client;
        }

        @Override
        public ServerPlayerData getServerPlayerData() {
            return data;
        }

        @Override
        public void setServerPlayerData(ServerPlayerData data) {
            this.data = data;
        }

        @Override
        public IServer getServer() {
            return socket.server;
        }
    }
}
