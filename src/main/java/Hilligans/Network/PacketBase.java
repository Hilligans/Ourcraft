package Hilligans.Network;

import Hilligans.Network.Packet.Client.*;
import Hilligans.Network.Packet.InvalidFormatPacket;
import Hilligans.Network.Packet.Server.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

public abstract class PacketBase {

    public static ArrayList<PacketFetcher> packets = new ArrayList<>();

    public ChannelHandlerContext ctx;


    public int packetId;

    public PacketBase(int id) {
        this.packetId = id;
    }

    public abstract void encode(PacketData packetData);

    public abstract void decode(PacketData packetData);

    public abstract void handle();

    public static void register() {
        packets.add(InvalidFormatPacket::new);
        packets.add(CRequestChunkPacket::new);
        packets.add(SSendChunkPacket::new);
        packets.add(CSendBlockChanges::new);
        packets.add(SSendBlockChanges::new);
        packets.add(CHandshakePacket::new);
        packets.add(SHandshakePacket::new);
        packets.add(CUpdatePlayerPacket::new);
        packets.add(SUpdateEntityPacket::new);
        packets.add(SCreateEntityPacket::new);
        packets.add(SRemoveEntityPacket::new);
        packets.add(CSendMessage::new);
        packets.add(SChatMessage::new);
        packets.add(COpenScreen::new);
        packets.add(SOpenScreen::new);
        packets.add(CUpdateContainer::new);
        packets.add(SUpdateContainer::new);
        packets.add(CActivateBlock::new);
        packets.add(SOpenContainer::new);
        packets.add(SUpdateInventory::new);


    }

    public PacketBase createNew() {
        return new InvalidFormatPacket();
    }


}
