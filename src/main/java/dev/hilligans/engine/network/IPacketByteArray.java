package dev.hilligans.engine.network;

import dev.hilligans.engine.util.IByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IPacketByteArray extends IByteArray {

    ByteBuf getByteBuf();
    int getPacketID();
    void setOwner(ChannelHandlerContext ctx);
    ChannelHandlerContext getOwner();

}
