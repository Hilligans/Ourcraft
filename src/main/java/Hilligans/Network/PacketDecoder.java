package Hilligans.Network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    int id = -1;
    public int dataLength = -1;
    public int packetLength = -1;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        if(in.readableBytes() >= 4) {
            packetLength = in.readableBytes();
            in.resetReaderIndex();
            in.markReaderIndex();
            dataLength = in.readInt();
            if (packetLength < dataLength) {
                if(packetLength >= 8) {
                    id = in.readInt();
                }
                in.resetReaderIndex();
                return;
            }
            byte[] bytes = new byte[dataLength - 4];
            in.readBytes(bytes);
            PacketData packetData = new PacketData(bytes);
            packetData.ctx = ctx;
            out.add(packetData);
            in.markReaderIndex();
        }
    }

    public float getPercentage() {
        return (float)dataLength / packetLength;
    }
}
