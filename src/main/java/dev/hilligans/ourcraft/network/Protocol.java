package dev.hilligans.ourcraft.network;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.data.primitives.IntArrayMap;
import dev.hilligans.ourcraft.data.primitives.IntArrayMapBuilder;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.network.packet.InvalidFormatPacket;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.network.packet.PacketType;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class Protocol implements IRegistryElement {

    public String protocolName;

    public ArrayList<PacketFetcher> packets = new ArrayList<>();
    public GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
    public ModContainer source;

    public ArrayList<PacketType> packetTypes = new ArrayList<>();

    public IntArrayMap map;
    public PacketType[] unmap;

    public Protocol(String protocolName) {
        this.protocolName = protocolName;
    }

    public void register(PacketType packetType) {
        packetTypes.add(packetType);
    }

    public int fromPacketTypeToId(int id) {
        return map.get(id);
    }

    public PacketType fromIdToPacketType(int id) {
        return unmap[id];
    }

    @Override
    public void load(GameInstance gameInstance) {
        IntArrayMapBuilder builder = new IntArrayMapBuilder(packetTypes.size(), 3);

        for(PacketType packetType : packetTypes) {
            builder.add(packetType.getRawID());
        }

        map = builder.build();
        unmap = new PacketType[map.size()];

        for(PacketType packetType : packetTypes) {
            unmap[map.get(packetType.getRawID())] = packetType;
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
