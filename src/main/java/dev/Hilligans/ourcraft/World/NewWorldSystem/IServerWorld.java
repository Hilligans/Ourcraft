package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Server.MultiPlayerServer;

public interface IServerWorld extends IWorld {

    BlockPos getWorldSpawn(BoundingBox boundingBox);

    void setServer(MultiPlayerServer server);
}
