package dev.hilligans.engine.network.debug;

import dev.hilligans.engine.network.IPacketByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.concurrent.ConcurrentHashMap;

public class PacketTracePacketEncoder extends MessageToByteEncoder<IPacketByteArray> {

    int packetWidth;
    boolean compressed;
    public ConcurrentHashMap<Long, PacketTrace> map;


    public PacketTracePacketEncoder(int packetWidth, boolean compressed, ConcurrentHashMap<Long, PacketTrace> map) {
        this.packetWidth = packetWidth;
        this.compressed = compressed;
        this.map = map;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IPacketByteArray msg, ByteBuf out) throws Exception {
        writeToByteBuf(out,msg,packetWidth,compressed);
    }

    public void writeToByteBuf(ByteBuf byteBuf, IPacketByteArray b, int packetWidth, boolean compressed) {
        if(b instanceof PacketTraceByteArray byteArray) {
            PacketTrace packetTrace = byteArray.packetTrace;
            packetTrace.totalLength = (int) (byteArray.getSize() + 4 + packetWidth + 8);
            map.put(byteArray.getUniversalPacketID(), packetTrace);
            byteBuf.writeInt((int) (byteArray.getSize() + 4 + packetWidth + 8));
            byteBuf.writeLong(byteArray.getUniversalPacketID());
            if (packetWidth == 1) {
                byteBuf.writeByte(byteArray.getPacketID());
            } else {
                byteBuf.writeShort(byteArray.getPacketID());
            }
            byteBuf.writeBytes(byteArray.getByteBuf());
        } else {
            throw new RuntimeException("Invalid packet type for use with packet tracer");
        }
    }
}
