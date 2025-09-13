package dev.hilligans.engine.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<IPacketByteArray> {

    int packetWidth;
    boolean compressed;


    public PacketEncoder(int packetWidth, boolean compressed) {
        this.packetWidth = packetWidth;
        this.compressed = compressed;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IPacketByteArray msg, ByteBuf out) throws Exception {
        writeToByteBuf(out,msg,packetWidth,compressed);
    }

    public void writeToByteBuf(ByteBuf byteBuf, IPacketByteArray byteArray, int packetWidth, boolean compressed) {
        byteBuf.writeInt((int) (byteArray.getSize() + 4 + packetWidth));
        if (compressed) {
           /* Deflater compressor = new Deflater();
            ByteBuffer buffer;
            if (packetWidth == 1) {
                buffer = this.byteBuf.nioBuffer(1, this.index);
                buffer.put(0, (byte) packetId);
            } else {
                buffer = this.byteBuf.nioBuffer(2, this.index);
                buffer.putShort(0, packetId);
            }
            compressor.setInput(buffer);
            compressor.finish();
            ByteBuffer newBuffer = ByteBuffer.allocate(4);
            int length = compressor.deflate(newBuffer);
            compressor.end();
            byteBuf.writeIntLE(length);
            byteBuf.writeBytes(newBuffer);

            */
        } else {
            if (packetWidth == 1) {
                byteBuf.writeByte(byteArray.getPacketID());
            } else {
                byteBuf.writeShort(byteArray.getPacketID());
            }
            byteBuf.writeBytes(byteArray.getByteBuf());
        }
    }
}
