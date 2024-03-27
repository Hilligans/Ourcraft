package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.network.debug.PacketTraceByteArray;
import dev.hilligans.ourcraft.ServerMain;
import io.netty.channel.*;

public abstract class NetworkHandler extends SimpleChannelInboundHandler<IPacketByteArray> {

    public Channel channel;
    public EventLoopGroup group;
    public String ip;
    public String port;
    public boolean enabled;
    public boolean debug = Ourcraft.getArgumentContainer().getBoolean("--tracePacket", false);

    public NetworkHandler setData(Channel channel, EventLoopGroup group, String ip, String port) {
        this.channel = channel;
        this.group = group;
        this.ip = ip;
        this.port = port;
        this.enabled = true;
        return this;
    }

    public ChannelFuture sendPacket(PacketBase<?> packetBase) {
        if(channel != null) {
            if(debug) {
                return channel.writeAndFlush(new PacketTraceByteArray(packetBase));
            } else {
                return channel.writeAndFlush(new PacketByteArray(packetBase));
            }
        } else {
            return null;
        }
    }

    public ChannelFuture shutdown() {
        group.shutdownGracefully();
        return channel.close();
    }
}
