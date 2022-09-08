package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;

public interface IServerWorld extends IWorld {

    BlockPos getWorldSpawn(BoundingBox boundingBox);

}
