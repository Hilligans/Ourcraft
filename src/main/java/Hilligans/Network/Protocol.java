package Hilligans.Network;

import Hilligans.Network.Packet.Client.CActivateBlock;
import Hilligans.Network.Packet.Client.CActivateButton;
import Hilligans.Network.Packet.Client.CSendBlockChanges;
import Hilligans.Network.Packet.InvalidFormatPacket;
import Hilligans.Network.Packet.Server.SChatMessage;
import Hilligans.Network.Packet.Server.SSendBlockChanges;
import Hilligans.Network.Packet.Server.SSendChunkPacket;
import Hilligans.Ourcraft;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Protocol {

    public String protocolName;

    public ArrayList<PacketFetcher> packets = new ArrayList<>();
    public HashMap<Class<PacketBase>, Integer> packetMap = new HashMap<>();
    public Int2BooleanOpenHashMap requiredIds = new Int2BooleanOpenHashMap();

    public void register(Supplier<PacketBase> packet) {
        register(new PacketFetcher(packets.size(),packet));
    }

    public void register(PacketFetcher packetFetcher) {
        for(int x = 1; x < packets.size(); x++) {
            if(packets.get(x).packetClass.equals(InvalidFormatPacket.class)) {
                packetMap.put(packetFetcher.packetClass, x);
                packets.set(x,packetFetcher);
                packetFetcher.id = x;
                return;
            }
        }
        packetFetcher.id = packets.size();
        packetMap.put(packetFetcher.packetClass, packets.size());
        packets.add(packetFetcher);
    }

    public void register(Supplier<PacketBase> packet, int id) {
        register(new PacketFetcher(id, packet),id);
    }

    public void register(PacketFetcher packetFetcher, int id) {
        if(!requiredIds.getOrDefault(id,false)) {
            while (id + 1 > packets.size()) {
                PacketFetcher packetFetcher1 = new PacketFetcher(packets.size(), InvalidFormatPacket::new);
                packetMap.put(packetFetcher1.packetClass, packets.size());
                packets.add(packetFetcher1);
            }
            requiredIds.put(id,true);
            packetMap.put(packetFetcher.packetClass, id);
            PacketFetcher oldPacket = packets.set(id, packetFetcher);
            if (!oldPacket.packetClass.equals(InvalidFormatPacket.class)) {
                register(oldPacket);
            }
        } else {
            Ourcraft.LOGGER.log(Level.WARNING,"Two packets were registered with the same id, " + packetFetcher.packetClass.toString().substring(6) + " tried to override packet " + packets.get(id).packetClass.toString().substring(6) + " with id " + id);
            register(packetFetcher);
        }
    }

    public Protocol(String protocolName) {
        this.protocolName = protocolName;
        register(InvalidFormatPacket::new,0);
    }

    public void mergeProtocols(Protocol protocol) {
        int x = 0;
        for(PacketFetcher packetFetcher : protocol.packets) {
            if(!packetFetcher.packetClass.equals(InvalidFormatPacket.class)) {
                if(requiredIds.get(x)) {
                    register(packetFetcher,x);
                } else {
                    register(packetFetcher);
                }
            }
            x++;
        }
    }

    public void clear() {
        packets.clear();
        packetMap.clear();
        requiredIds.clear();
        register(InvalidFormatPacket::new,0);
    }

    @Override
    public String toString() {
        return "Protocol{" +
                "protocolName='" + protocolName + '\'' +
                ", packets=" + packets +
                ", packetMap=" + packetMap +
                ", requiredIds=" + requiredIds +
                '}';
    }
}
