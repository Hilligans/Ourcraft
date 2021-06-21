package Hilligans.Network;

import Hilligans.Data.Other.ColoredString;
import Hilligans.Item.Item;
import Hilligans.Item.ItemStack;
import Hilligans.Item.Items;
import Hilligans.Network.Packet.AuthServerPackets.SAccountPacket;
import Hilligans.Network.Packet.AuthServerPackets.SSendLoginToken;
import Hilligans.Network.Packet.AuthServerPackets.SSendToken;
import Hilligans.Network.Packet.AuthServerPackets.STokenValid;
import Hilligans.Util.ByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.joml.Vector4f;

import java.awt.image.BufferedImage;

public class PacketData extends ByteArray {


    public ChannelHandlerContext ctx;
    int packetId = 0;

    public PacketData(PacketBase packetBase) {
        byteBuf = Unpooled.buffer();
        packetId = packetBase.packetId;
        packetBase.encode(this);
    }

    public PacketData(int val) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeByte(val);
    }

    public PacketData(byte[] bytes) {
        byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(bytes);
        packetId = byteBuf.readInt();
    }

    public PacketData(ByteBuf byteBuf) {
        packetId = byteBuf.readInt();
        byteBuf.readBytes(packetId);
    }

    public void writeToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(size + 8);
        byteBuf.writeInt(packetId);
        byteBuf.writeBytes(this.byteBuf);
    }

    public PacketBase createPacket() {
        PacketBase packetBase = PacketBase.packets.get(packetId).getPacket();
        packetBase.ctx = ctx;
        packetBase.decode(this);
        return packetBase;
    }

    public PacketBase createAltPacket() {
        PacketBase packetBase;
        if(packetId == 0) {
            packetBase = new SAccountPacket();
        } else if(packetId == 1) {
            packetBase = new SSendToken();
        } else if(packetId == 2) {
            packetBase = new STokenValid();
        } else {
            packetBase = new SSendLoginToken();
        }
        packetBase.ctx = ctx;
        packetBase.decode(this);
        return packetBase;
    }




}
