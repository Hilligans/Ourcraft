package dev.hilligans.ourcraft.network;

import java.util.function.Supplier;

public class PacketFetcher {


    public int id;
    public Supplier<PacketBase<?>> packet;
    public Class<PacketBase<?>> packetClass;

    public PacketFetcher(int id, Supplier<PacketBase<?>> packet) {
        this.packet = packet;
        this.id = id;
        packetClass = (Class<PacketBase<?>>) packet.get().getClass();
    }

    public PacketBase<?> getPacket() {
        PacketBase<?> packetBase = packet.get();
        packetBase.packetId = id;
        return packetBase;
    }

    @Override
    public String toString() {
        return "PacketFetcher{" +
                "id=" + id +
                ", packetClass=" + packetClass +
                '}';
    }
}
