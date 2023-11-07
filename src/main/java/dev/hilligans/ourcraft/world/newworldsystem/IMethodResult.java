package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.data.other.BlockPos;

public interface IMethodResult {


    void queueUpdate(int x, int y, int z);

    void queueUpdate(BlockPos pos);

    void queueSixUpdates(int x, int y, int z);

    void queueSixUpdates(BlockPos pos);

    void scheduleTick(int x, int y, int z, Block block, int delay);

    void scheduleTick(BlockPos pos, Block block, int delay);


}
