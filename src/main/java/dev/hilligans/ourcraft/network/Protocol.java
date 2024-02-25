package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.network.packet.InvalidFormatPacket;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class Protocol implements IRegistryElement {

    public String protocolName;

    public ArrayList<PacketFetcher> packets = new ArrayList<>();
    public HashMap<Class<PacketBase>, Integer> packetMap = new HashMap<>();
    public Int2BooleanOpenHashMap requiredIds = new Int2BooleanOpenHashMap();
    public boolean compressed;
    public GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
    public ModContainer source;

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
            gameInstance.LOGGER.warn("Two packets were registered with the same id, " + packetFetcher.packetClass.toString().substring(6) + " tried to override packet " + packets.get(id).packetClass.toString().substring(6) + " with id " + id);
            register(packetFetcher);
        }
    }

    public Protocol(String protocolName) {
        this.protocolName = protocolName;
        register(InvalidFormatPacket::new);
    }

    public Protocol setSource(ModContainer container) {
        this.source = container;
        return this;
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
        register(InvalidFormatPacket::new);
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

    @Override
    public void assignOwner(ModContainer source) {
        this.source = source;
    }

    @Override
    public String getResourceName() {
        return protocolName;
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "protocol";
    }
}
