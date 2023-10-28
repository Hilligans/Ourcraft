package dev.hilligans.ourcraft.World.Modifications;

import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Server.Concurrent.Lock;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;

public interface IWorldModification {

    void apply(Lock lock, IChunk chunk);

    BlockPos getMin();
    BlockPos getMax();

    default long getMinX() {
        return getMin().x;
    }
    default long getMinY() {
        return getMin().y;
    }
    default long getMinZ() {
        return getMin().z;
    }
    default long getMaxX() {
        return getMax().x;
    }
    default long getMaxY() {
        return getMax().y;
    }
    default long getMaxZ() {
        return getMax().z;
    }

    default public boolean isWholeInside(IChunk chunk) {
        return  (getMinX() <= chunk.getBlockX() && getMinY() <= chunk.getBlockY() && getMinZ() <= chunk.getBlockZ()) &&
                (getMaxX() >= chunk.getBlockMaxX() && getMaxY() >= chunk.getBlockMaxY() && getMaxZ() >= chunk.getBlockMaxZ());
    }
}
