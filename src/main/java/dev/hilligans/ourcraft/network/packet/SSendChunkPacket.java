package dev.hilligans.ourcraft.network.packet;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.engine.network.engine.ClientNetworkEntity;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.network.ServerToClientPacketType;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.world.newworldsystem.CubicChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public class SSendChunkPacket extends ServerToClientPacketType {

    public static final SSendChunkPacket instance = new SSendChunkPacket();

    public static void send(NetworkEntity entity, IChunk chunk) {
        entity.sendPacket(instance.encode(entity, chunk));
    }

    public static IByteArray get(NetworkEntity networkEntity, IChunk chunk) {
        return instance.encode(networkEntity, chunk);
    }

    public IByteArray encode(NetworkEntity entity, IChunk chunk) {
        IByteArray array = getWriteArray(entity);
        Ourcraft.chainedChunkStream.fillBuffer(array, chunk);
        return array;
    }

    @Override
    public void decode(ClientNetworkEntity<Client> entity, IByteArray data) {
        IWorld world = entity.getClient().getWorld();
        IChunk chunk = new CubicChunk(world, 32,0,0, 0);
        Ourcraft.chainedChunkStream.fillChunk(data, chunk);
        world.setChunk(chunk.getBlockX(), chunk.getBlockY(),  chunk.getBlockZ(), chunk.setWorld(world));
        world.markDirtyAround(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ());
    }
}
