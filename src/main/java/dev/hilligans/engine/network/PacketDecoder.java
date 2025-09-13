package dev.hilligans.engine.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    int id = -1;
    public int dataLength = -1;
    public int packetLength = -1;
    public int packetWidth = 2;
    public boolean compressed;

    public PacketDecoder(int packetWidth, boolean compressed) {
        this.packetWidth = packetWidth;
        this.compressed = compressed;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        packetLength = in.readableBytes();
        if(compressed) {
            if(packetLength >= 5) {

            }
        } else {
            if (packetLength >= 4 + packetWidth) {
                in.resetReaderIndex();
                in.markReaderIndex();
                dataLength = in.readInt();
                if (packetLength < dataLength) {
                    in.resetReaderIndex();
                    return;
                }
                byte[] bytes = new byte[dataLength - 4];
                in.readBytes(bytes);
                IPacketByteArray packetData = new PacketByteArray(bytes, packetWidth);
                packetData.setOwner(ctx);
                out.add(packetData);
                in.markReaderIndex();
            }
        }
    }

    public float getPercentage() {
        return (float)dataLength / packetLength;
    }

}
