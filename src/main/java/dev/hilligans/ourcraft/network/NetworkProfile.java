package dev.hilligans.ourcraft.network;

import io.netty.channel.Channel;

public interface NetworkProfile {

    void setSendProtocol(Protocol protocol);

    void setReceiveProtocol(Protocol protocol);

    Protocol getSendProtocol();

    Protocol getReceiveProtocol();

    void setChannel(Channel channel);



    Channel getChannel();
}
