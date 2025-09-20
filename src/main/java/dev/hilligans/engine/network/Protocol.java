package dev.hilligans.engine.network;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.data.IntArrayMap;
import dev.hilligans.engine.data.IntArrayMapBuilder;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.network.packet.PacketType;
import dev.hilligans.engine.util.registry.IRegistryElement;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.ArrayList;

public class Protocol implements IRegistryElement {

    public String protocolName;

    public ModContainer source;

    public ArrayList<Tuple<PacketType<?>, Integer>> packetTypes = new ArrayList<>();
    public boolean setIDs;
    public int maxID;

    public Int2IntOpenHashMap mapper;
    public IntArrayMap map;
    public PacketType<?>[] unmap;

    public Protocol(String protocolName) {
        this.protocolName = protocolName;
    }

    public void register(PacketType<?> packetType) {
        packetTypes.add(new Tuple<>(packetType, -1));
    }

    public void register(PacketType<?> packetType, int id) {
        packetTypes.add(new Tuple<>(packetType, id));
        setIDs = true;
        maxID = Math.max(maxID, id);
    }

    public int fromPacketTypeToId(int id) {
        if(map == null) {
            return mapper.get(id);
        } else {
            return map.get(id);
        }
    }

    public PacketType<?> fromIdToPacketType(int id) {
        return unmap[id];
    }

    @Override
    public void load(GameInstance gameInstance) {
        if(setIDs) {
            mapper = new Int2IntOpenHashMap();
            unmap = new PacketType[maxID+1];

            for (Tuple<PacketType<?>, Integer> packetType : packetTypes) {
                if(packetType.getTypeB() == -1) {
                    throw new RuntimeException("Cannot use mixed packet types");
                }
                mapper.put(packetType.getTypeA().getRawID(), (int)packetType.getTypeB());
                unmap[packetType.getTypeB()] = packetType.getTypeA();
            }
        } else {
            IntArrayMapBuilder builder = new IntArrayMapBuilder(packetTypes.size(), 3);

            for (Tuple<PacketType<?>, Integer> packetType : packetTypes) {
                builder.add(packetType.getTypeA().getRawID());
            }

            map = builder.build();
            unmap = new PacketType[map.size()];

            for (Tuple<PacketType<?>, Integer> packetType : packetTypes) {
                unmap[map.get(packetType.getTypeA().getRawID())] = packetType.getTypeA();
            }
        }
    }

    public Protocol setSource(ModContainer container) {
        this.source = container;
        return this;
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
