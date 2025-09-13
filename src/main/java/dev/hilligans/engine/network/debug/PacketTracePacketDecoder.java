package dev.hilligans.engine.network.debug;

import dev.hilligans.engine.network.IPacketByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PacketTracePacketDecoder extends ByteToMessageDecoder {

    int id = -1;
    public int dataLength = -1;
    public int packetLength = -1;
    public int packetWidth = 2;
    public boolean compressed;
    ConcurrentHashMap<Long, PacketTrace> map;

    public PacketTracePacketDecoder(int packetWidth, boolean compressed, ConcurrentHashMap<Long, PacketTrace> map) {
        this.packetWidth = packetWidth;
        this.compressed = compressed;
        this.map = map;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        packetLength = in.readableBytes();
        if (packetLength >= 4 + 8 + packetWidth) {
            in.resetReaderIndex();
            in.markReaderIndex();
            dataLength = in.readInt();
            if (packetLength < dataLength) {
                in.resetReaderIndex();
                return;
            }
            long univPacketID = in.readLong();
            byte[] bytes = new byte[dataLength - 4 - 8];
            in.readBytes(bytes);
            PacketTrace packetTrace = map.remove(univPacketID);
            if(packetTrace.totalLength != dataLength) {
                throw new RuntimeException("Lengths do not match, expected: " + packetTrace.totalLength + " but got: " + dataLength);
            }
            //System.out.println("Length:" + dataLength);
            IPacketByteArray packetData = new PacketTraceByteArray(bytes, packetWidth, packetTrace);
            packetData.setOwner(ctx);
            out.add(packetData);
            in.markReaderIndex();
        }
    }

    public float getPercentage() {
        return (float)dataLength / packetLength;
    }

}
