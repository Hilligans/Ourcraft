package Hilligans.Network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        int length = in.readableBytes();
        in.resetReaderIndex();
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (length < dataLength) {
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
