package dev.hilligans.ourcraft.Network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class NetworkHandler extends SimpleChannelInboundHandler<PacketData> {

    public Channel channel;
    public EventLoopGroup group;
    public String ip;
    public String port;
    public boolean enabled;

    public NetworkHandler setData(Channel channel, EventLoopGroup group, String ip, String port) {
        this.channel = channel;
        this.group = group;
        this.ip = ip;
        this.port = port;
        this.enabled = true;
        return this;
    }

    public ChannelFuture sendPacket(PacketBase packetBase) {
        if(channel != null) {
            return channel.writeAndFlush(new PacketData(packetBase));
        } else {
            return null;
        }
    }

    public ChannelFuture shutdown() {
        group.shutdownGracefully();
        return channel.close();
    }
}
