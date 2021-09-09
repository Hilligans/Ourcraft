package Hilligans.Network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<PacketData> {

    int packetWidth;
    boolean compressed;


    public PacketEncoder(int packetWidth, boolean compressed) {
        this.packetWidth = packetWidth;
        this.compressed = compressed;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, PacketData msg, ByteBuf out) throws Exception {
        msg.writeToByteBuf(out,packetWidth,compressed);
    }
}
